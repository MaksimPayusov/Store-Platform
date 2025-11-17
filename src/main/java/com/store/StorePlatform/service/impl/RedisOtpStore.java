package com.store.StorePlatform.service.impl;

import com.store.StorePlatform.service.OtpStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class RedisOtpStore implements OtpStore {

    private static final String PREFIX = "auth:otp:";

    private final StringRedisTemplate redisTemplate;

    @Override
    public void saveOtp(String email, String otp, Duration ttl) {
        redisTemplate.opsForValue().set(key(email), otp, ttl);
    }

    @Override
    public String getOtp(String email) {
        return redisTemplate.opsForValue().get(key(email));
    }

    @Override
    public void deleteOtp(String email) {
        redisTemplate.delete(key(email));
    }

    private String key(String email) {
        return PREFIX + email.toLowerCase();
    }
}

