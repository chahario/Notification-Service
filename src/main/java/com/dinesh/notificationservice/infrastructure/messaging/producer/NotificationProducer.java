package com.dinesh.notificationservice.infrastructure.messaging.producer;

import com.dinesh.notificationservice.infrastructure.messaging.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate; // core spring kafka class used to send the msg
    // key -> String , NotificationEvent -> value

    private static final String TOPIC = "notifications-events";

    public void send(NotificationEvent event) {
        log.debug("Sending notification event to kafka notificationId: {}",
                event.getNotificationId()
        );
        String key = event.getUserId() + "_" + event.getType(); // ensure ordering
        kafkaTemplate.send(
                TOPIC,
                key, // key
                event);  // value
    }
}
