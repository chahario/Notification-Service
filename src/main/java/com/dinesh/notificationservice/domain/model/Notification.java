package com.dinesh.notificationservice.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(name = "idx_user_id", columnList = "userId"),
                @Index(name = "idx_status", columnList = "status"),
                @Index(name = "idx_idempotency_key", columnList = "idempotencyKey", unique = true),
                @Index(name = "idx_retry_notifications",columnList = "nextRetryTime")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private int retryCount;

    private Instant nextRetryTime;

    /**
     * Ensures idempotency.
     * Same request cannot create duplicate notifications.
     */
    @Column(nullable = false, unique = true)
    private String idempotencyKey;

    /**
     * Provider response (SendGrid, Twilio etc.)
     */
    private String providerResponse;

    /**
     * Error message in case of failure
     */
    private String errorMessage;

    /**
     * Auditing fields
     */
    private Instant createdAt;

    private Instant updatedAt;


    /**
     * Lifecycle hooks
     */
    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (status == null) {
            status = NotificationStatus.PENDING;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public void incrementRetry() {
        retryCount++;
    }
}