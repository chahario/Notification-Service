package com.dinesh.notificationservice.infrastructure.messaging.consumer;

import com.dinesh.notificationservice.infrastructure.messaging.event.NotificationEvent;
import com.dinesh.notificationservice.worker.processor.NotificationProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationProcessor processor;

    @KafkaListener(
            topics = "notification-events",
            groupId = "notification-group"
    )
    public void consume(NotificationEvent event) {
        processor.process(event);
    }
}
