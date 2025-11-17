package com.store.StorePlatform.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("!prod")
public class LoggingOtpNotificationService implements OtpNotificationService {

    @Override
    public void sendOtp(String email, String otp) {
        log.info("OTP code {} sent to {}", otp, email);
    }
}

