package com.dinesh.notificationservice.api.dto;

import com.dinesh.notificationservice.domain.model.NotificationType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {

    private String userId;

    private NotificationType type;

    private String payload;

    private String idempotencyKey;
}