package com.dinesh.notificationservice.domain.service;

import com.dinesh.notificationservice.domain.model.*;
import com.dinesh.notificationservice.infrastructure.messaging.event.NotificationEvent;
import com.dinesh.notificationservice.infrastructure.repository.NotificationRepository;
import com.dinesh.notificationservice.infrastructure.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Notification createNotification(
            String userId,
            NotificationType type,
            String payload,
            String idempotencyKey
    ) {
        log.info("Creating notification userId={} type={} idempotencyKey={}",
                userId,
                type,
                idempotencyKey);

        Optional<Notification> existing =
                notificationRepository.findByIdempotencyKey(idempotencyKey); // checking by idempotency key

        if (existing.isPresent()) {
            log.warn(
                    "Duplicate Notification request detected idempotencyKey={}",
                    idempotencyKey
            );
            return existing.get();
        }

        Notification notification = Notification.builder()
                .userId(userId)
                .type(type)
                .payload(payload)
                .status(NotificationStatus.PENDING)
                .retryCount(0)
                .idempotencyKey(idempotencyKey)
                .build();

        Notification saved = notificationRepository.save(notification); // saving to db
        log.info("Notification persisted id={}",
                saved.getId()
        );

        NotificationEvent event = NotificationEvent.builder()
                .notificationId(saved.getId())
                .userId(saved.getUserId())
                .type(saved.getType())
                .payload(saved.getPayload())
                .build();
        log.info("Notification event created notificationID = {}",
                saved.getId()
        );

        try{
            OutboxEvent outbox = OutboxEvent.builder()
                    .aggregateType("Notification")
                    .aggregateId(saved.getId())
                    .eventType("NotificationCreated")
                    .payload(objectMapper.writeValueAsString(event))
                    .status("PENDING")
                    .build();
            outboxRepository.save(outbox);
            log.info("Outbox Event created notificationID = {}",
                    saved.getId()
            );
        }catch (Exception e){
            log.error("Error while saving Outbox event for notificationId:{} . Error: {}",
                    saved.getId(),e.getMessage(),e);
            throw new RuntimeException("Transaction rolled back: Failed to persist outbox event",e);
        }
        return saved;
    }
}