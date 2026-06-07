package com.dinesh.notificationservice.infrastructure.messaging.event;

import com.dinesh.notificationservice.domain.model.NotificationType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {
    private Long notificationId;
    private String userId;
    private NotificationType type;
    private String payload;
}
