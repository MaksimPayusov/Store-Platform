package com.store.StorePlatform.service;

import com.store.StorePlatform.dto.request.ConfirmLoginRequest;
import com.store.StorePlatform.dto.request.LoginRequest;
import com.store.StorePlatform.entity.User;
import com.store.StorePlatform.service.token.AuthTokens;
import com.store.StorePlatform.service.token.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final OtpService otpService;
    private final JwtTokenService jwtTokenService;
    private final Clock clock;

    public void requestOtp(LoginRequest request) {
        User user = userService.getOrCreate(request.email());
        if (!user.isActive()) {
            throw new IllegalStateException("User is blocked");
        }
        otpService.sendOtp(user.getEmail());
    }

    @Transactional
    public AuthTokens confirmOtp(ConfirmLoginRequest request) {
        otpService.validateOtp(request.email(), request.otp());
        User user = userService.updateLastLogin(request.email(), clock.instant());
        return jwtTokenService.generateTokens(user);
    }
}

