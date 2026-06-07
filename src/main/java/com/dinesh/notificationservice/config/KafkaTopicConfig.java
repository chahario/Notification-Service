package com.dinesh.notificationservice.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic notificationTopic() {
        return new NewTopic("notification-events", 3, (short) 1);
    }
//topic name ,partition count ,replication factor

    @Bean
    public NewTopic notificationDlqTopic() {
        return new NewTopic("notification-dlq", 3, (short) 1);
    }
}
