package com.dinesh.notificationservice.worker.channel;

import com.dinesh.notificationservice.domain.model.Notification;
import com.dinesh.notificationservice.domain.model.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class PushChannel implements NotificationChannel {
    @Override
    public NotificationType getType(){
        return NotificationType.PUSH;
    }
    @Override
    public void send(Notification notification) {
        System.out.println("Sending PUSH: "+ notification.getPayload());
    }
}
