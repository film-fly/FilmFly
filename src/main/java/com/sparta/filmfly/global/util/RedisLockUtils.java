package com.sparta.filmfly.global.util;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisLockUtils {

    private final RedisTemplate<String, String> redisTemplate;

    public boolean acquireLock(String lockKey) {
        long defaultLockExpiration = Duration.ofSeconds(30).toMillis();
        return acquireLock(lockKey, defaultLockExpiration);
    }

    public boolean acquireLock(String lockKey, long expirationMillis) {
        Boolean lockAcquired = redisTemplate
            .opsForValue()
            .setIfAbsent(lockKey, "locked", Duration.ofMillis(expirationMillis));
        return lockAcquired != null && lockAcquired;
    }

    public void releaseLock(String lockKey) {
        redisTemplate.delete(lockKey);
    }
}