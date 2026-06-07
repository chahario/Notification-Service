package com.dinesh.notificationservice.api.dto;

import com.dinesh.notificationservice.domain.model.NotificationStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long notificationId;

    private NotificationStatus status;
}