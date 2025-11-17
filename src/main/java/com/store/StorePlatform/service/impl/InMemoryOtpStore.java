package com.store.StorePlatform.service.impl;

import com.store.StorePlatform.service.OtpStore;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("test")
public class InMemoryOtpStore implements OtpStore {

    private final Map<String, Entry> storage = new ConcurrentHashMap<>();

    @Override
    public void saveOtp(String email, String otp, Duration ttl) {
        storage.put(email, new Entry(otp, Instant.now().plus(ttl)));
    }

    @Override
    public String getOtp(String email) {
        Entry entry = storage.get(email);
        if (entry == null || Instant.now().isAfter(entry.expiresAt())) {
            storage.remove(email);
            return null;
        }
        return entry.otp();
    }

    @Override
    public void deleteOtp(String email) {
        storage.remove(email);
    }

    private record Entry(String otp, Instant expiresAt) {
    }
}

