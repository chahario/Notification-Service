package com.dinesh.notificationservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "notification.retry")
public class RetryProperties {
    private int maxAttempts;
    private long baseDelayMs;
    private long pollInterval;

}

