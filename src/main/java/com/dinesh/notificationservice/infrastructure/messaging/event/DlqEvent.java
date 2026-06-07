package com.dinesh.notificationservice.infrastructure.messaging.event;

import com.dinesh.notificationservice.domain.model.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class DlqEvent {
    private long notificationId;
    private String userId;
    private NotificationType type;
    private String payload;
    private String errorMessage;
    private int retryCount;
    private Instant failedAt;
}
