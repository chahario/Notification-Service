package com.dinesh.notificationservice.worker.channel;

import com.dinesh.notificationservice.domain.model.Notification;
import com.dinesh.notificationservice.domain.model.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class EmailChannel implements NotificationChannel {

    @Override
    public NotificationType getType(){
        return NotificationType.EMAIL;
    }
    @Override
    public void send(Notification notification) {
        System.out.println("Sending notification through EMAIL: " + notification.getPayload());
    }
}
