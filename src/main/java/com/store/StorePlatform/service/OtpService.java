package com.store.StorePlatform.service;

import com.store.StorePlatform.config.properties.OtpProperties;
import com.store.StorePlatform.exception.InvalidOtpException;
import com.store.StorePlatform.service.notification.OtpNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpStore otpStore;
    private final OtpProperties otpProperties;
    private final OtpNotificationService notificationService;
    private final SecureRandom secureRandom;

    public void sendOtp(String email) {
        String otp = generateOtp();
        otpStore.saveOtp(email, otp, otpProperties.getTtl());
        notificationService.sendOtp(email, otp);
    }

    public void validateOtp(String email, String otp) {
        String stored = otpStore.getOtp(email);
        if (stored == null || !stored.equals(otp)) {
            throw new InvalidOtpException("Invalid or expired OTP code");
        }
        otpStore.deleteOtp(email);
    }

    private String generateOtp() {
        int bound = (int) Math.pow(10, otpProperties.getLength());
        int min = bound / 10;
        int code = secureRandom.nextInt(bound - min) + min;
        return String.format("%0" + otpProperties.getLength() + "d", code);
    }
}

