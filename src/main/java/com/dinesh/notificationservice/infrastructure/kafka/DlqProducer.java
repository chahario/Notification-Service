package com.dinesh.notificationservice.infrastructure.kafka;

import com.dinesh.notificationservice.infrastructure.messaging.event.DlqEvent;
import com.dinesh.notificationservice.infrastructure.messaging.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DlqProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String DLQ_TOPIC = "notification-dlq";

    public void send(DlqEvent event) {
        kafkaTemplate.send(
                DLQ_TOPIC,
                event.getUserId(), // partitioning by
                event
        );
    }
}
