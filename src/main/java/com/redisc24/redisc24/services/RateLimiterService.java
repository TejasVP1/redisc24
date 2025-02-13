package com.redisc24.redisc24.services;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;
    private static final int REQUEST_LIMIT = 5;
    private static final int WINDOW_DURATION = 60;

    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String clientId) {
        String key = "rate_limit:" + clientId + ":" + getCurrentWindow();
        Long currentCount = redisTemplate.opsForValue().increment(key, 1);

        if (currentCount == 1) {

            redisTemplate.expire(key, WINDOW_DURATION, TimeUnit.SECONDS);
        }

        return currentCount <= REQUEST_LIMIT;
    }

    private long getCurrentWindow() {
        return Instant.now().getEpochSecond() / WINDOW_DURATION;
    }
}
