package com.store.StorePlatform.service;

import java.time.Duration;

public interface OtpStore {

    void saveOtp(String email, String otp, Duration ttl);

    String getOtp(String email);

    void deleteOtp(String email);
}

