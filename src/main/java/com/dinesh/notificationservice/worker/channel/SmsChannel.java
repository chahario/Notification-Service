package com.dinesh.notificationservice.worker.channel;

import com.dinesh.notificationservice.domain.model.Notification;
import com.dinesh.notificationservice.domain.model.NotificationType;
import org.springframework.stereotype.Component;
@Component
public class SmsChannel implements NotificationChannel {

    @Override
    public NotificationType getType(){
        return NotificationType.SMS;
    }
    @Override
    public void send(Notification notification) {
        System.out.println("Sending SMS: "+ notification.getPayload());
    }
}
