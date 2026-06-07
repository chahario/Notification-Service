package com.dinesh.notificationservice.worker.publisher;

import com.dinesh.notificationservice.domain.model.OutboxEvent;
import com.dinesh.notificationservice.infrastructure.messaging.event.NotificationEvent;
import com.dinesh.notificationservice.infrastructure.messaging.producer.NotificationProducer;
import com.dinesh.notificationservice.infrastructure.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxRepository outboxRepository;
    private final NotificationProducer producer;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 5000)
    public void publish() {
        List<OutboxEvent> events =
                outboxRepository.findTop100ByStatusOrderByCreatedAt("PENDING");
        log.debug("Found {} outbox events to Publish", events.size());

        for (OutboxEvent event : events) {

            try {
                event.setStatus("PROCESSING");
                outboxRepository.save(event);

                NotificationEvent notificationEvent =
                        objectMapper.readValue(
                                event.getPayload(),
                                NotificationEvent.class
                        );
                producer.send(notificationEvent);
                event.setStatus("SENT");
                log.info(
                        "Outbox event published aggregateId={}",
                        event.getAggregateId()
                );
                outboxRepository.save(event);

            } catch (Exception e) {
                log.error(
                        "Failed to publish outbox event aggregateId={}",
                        event.getAggregateId(),
                        e
                );
                event.setStatus("FAILED");

                outboxRepository.save(event);
            }

        }
    }
}
