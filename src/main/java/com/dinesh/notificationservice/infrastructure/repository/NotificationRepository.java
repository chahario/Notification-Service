package com.dinesh.notificationservice.infrastructure.repository;

import com.dinesh.notificationservice.domain.model.Notification;
import com.dinesh.notificationservice.domain.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByIdempotencyKey(String idempotencyKey);

    List<Notification> findTop100ByStatusOrderByCreatedAt(NotificationStatus status);

    List<Notification> findByStatus(NotificationStatus status);

    @Query("""
    SELECT n from Notification n
    WHERE n.status = 'RETRYING'
    AND n.nextRetryAt <= :now
    """)
    List<Notification>findReadyForRetry(Instant now);

}