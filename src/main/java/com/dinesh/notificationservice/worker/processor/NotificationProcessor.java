package com.dinesh.notificationservice.worker.processor;

import com.dinesh.notificationservice.config.RetryProperties;
import com.dinesh.notificationservice.domain.model.Notification;
import com.dinesh.notificationservice.domain.model.NotificationStatus;
import com.dinesh.notificationservice.infrastructure.kafka.DlqProducer;
import com.dinesh.notificationservice.infrastructure.messaging.event.DlqEvent;
import com.dinesh.notificationservice.infrastructure.messaging.event.NotificationEvent;
import com.dinesh.notificationservice.infrastructure.repository.NotificationRepository;
import com.dinesh.notificationservice.worker.channel.NotificationChannel;
import com.dinesh.notificationservice.worker.factory.ChannelFactory;
import com.dinesh.notificationservice.worker.retry.RetryBackoffCalculator;
import lombok.RequiredArgsConstructor; // will write constructor (with final keywords)
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class NotificationProcessor {
    private final ChannelFactory channelFactory;
    private final NotificationRepository notificationRepository;
    private final RetryProperties retryProperties;
    private final RetryBackoffCalculator retryBackoffCalculator;
    private final DlqProducer dlqProducer;


    public void process(NotificationEvent event) {
        Notification notification = notificationRepository.findById(event.getNotificationId())
                .orElseThrow();
        notification.setStatus(NotificationStatus.PROCESSING);
        notificationRepository.save(notification);

        NotificationChannel channel = channelFactory.getChannel(notification.getType());

        try {
            channel.send(notification);
            notification.setStatus(NotificationStatus.SENT);
        }catch (Exception e){
            notification.incrementRetry();
            notification.setErrorMessage(e.getMessage());
            if (notification.getRetryCount() >= retryProperties.getMaxAttempts()) {
                notification.setStatus(NotificationStatus.FAILED);
                DlqEvent dlqEvent = DlqEvent.builder()
                        .notificationId(notification.getId())
                        .userId(notification.getUserId())
                        .type(notification.getType())
                        .payload(notification.getPayload())
                        .errorMessage(notification.getErrorMessage())
                        .retryCount(notification.getRetryCount())
                        .failedAt(Instant.now())
                        .build();
                dlqProducer.send(dlqEvent);
            } else{
                long delay =
                        retryBackoffCalculator.calculateDelay(
                                retryProperties.getBaseDelayMs(),
                                notification.getRetryCount()
                        );
                Instant nextRetry = Instant.now().plusMillis(delay);

                notification.setNextRetryTime(nextRetry);
                notification.setStatus(NotificationStatus.RETRYING);
            }

        }
        notificationRepository.save(notification);
    }
}
