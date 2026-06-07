package com.dinesh.notificationservice.worker.retry;

import com.dinesh.notificationservice.domain.model.Notification;
import com.dinesh.notificationservice.domain.model.NotificationStatus;
import com.dinesh.notificationservice.infrastructure.messaging.event.NotificationEvent;
import com.dinesh.notificationservice.infrastructure.messaging.producer.NotificationProducer;
import com.dinesh.notificationservice.infrastructure.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
@Component
@RequiredArgsConstructor
public class RetryWorker {

    private final NotificationRepository repository;
    private final NotificationProducer producer;

    @Scheduled(fixedDelayString = "${notification.retry.poll-interval}")
    public void retryFailedNotifications(){

        List<Notification> notifications = repository.
                findReadyForRetry(Instant.now());
        for (Notification notification : notifications) {
            NotificationEvent event =
                    NotificationEvent.builder()
                            .notificationId(notification.getId())
                            .userId(notification.getUserId())
                            .type(notification.getType())
                            .payload(notification.getPayload())
                            .build();
            producer.send(event);
        }
    }
}
