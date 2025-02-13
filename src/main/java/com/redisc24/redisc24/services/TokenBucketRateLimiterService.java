package com.redisc24.redisc24.services;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBucketRateLimiterService {

    private final StringRedisTemplate redisTemplate;
    private static final int MAX_TOKENS = 5;
    private static final int REFILL_RATE = 1;

    public TokenBucketRateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String clientId) {
        String key = "token_bucket:" + clientId;


        String tokenCountStr = redisTemplate.opsForValue().get(key);
        int tokenCount = (tokenCountStr == null) ? MAX_TOKENS : Integer.parseInt(tokenCountStr);

        if (tokenCount > 0) {

            redisTemplate.opsForValue().set(key, String.valueOf(tokenCount - 1), MAX_TOKENS / REFILL_RATE, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }
}
