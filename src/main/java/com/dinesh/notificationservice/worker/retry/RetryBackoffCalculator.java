package com.dinesh.notificationservice.worker.retry;

import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadLocalRandom; // better performance in multithreaded envs

@Component
public class RetryBackoffCalculator {
    public long calculateDelay(long baseDelay, int retryCount){
        long maxDelay =
                baseDelay * (long) Math.pow(2, retryCount);
        return ThreadLocalRandom
                .current()
                .nextLong(maxDelay + 1);
    }
}
