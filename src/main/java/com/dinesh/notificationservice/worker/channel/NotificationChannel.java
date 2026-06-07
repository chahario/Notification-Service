package com.dinesh.notificationservice.worker.channel;

import com.dinesh.notificationservice.domain.model.Notification;
import com.dinesh.notificationservice.domain.model.NotificationType;

public interface NotificationChannel {
    NotificationType getType();
    void send(Notification notification);
}
