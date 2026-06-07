package com.dinesh.notificationservice.worker.factory;

import com.dinesh.notificationservice.domain.model.NotificationType;
import com.dinesh.notificationservice.worker.channel.NotificationChannel;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.List;


@Component
public class ChannelFactory {
    private final Map<NotificationType, NotificationChannel> registry; // thread safe (final keyword)

    // when spring sees List<NotificationChannel> in the constructor it searches entire application for any
    // class that implements NotificationChannel interface , it collects them all and passes them into a list.
    public ChannelFactory(List<NotificationChannel> channels){ // constructor
        registry = new EnumMap<>(NotificationType.class); // high performant Map implementation, specifically
        // used when keys are Enums, faster and uses less memory than HASHMAP.

        for(NotificationChannel channel : channels){
            registry.put(channel.getType(), channel);
        }
    }
    public NotificationChannel getChannel(NotificationType type){
        NotificationChannel channel = registry.get(type);
        if(channel == null){
            throw new IllegalArgumentException("Unsupported notification type: " + type);
        }
        return channel;
    }
}
