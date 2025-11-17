package com.store.StorePlatform.service;

import com.store.StorePlatform.dto.request.ConfirmLoginRequest;
import com.store.StorePlatform.dto.request.LoginRequest;
import com.store.StorePlatform.entity.User;
import com.store.StorePlatform.service.token.AuthTokens;
import com.store.StorePlatform.service.token.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private final UserService userService = mock(UserService.class);
    private final OtpService otpService = mock(OtpService.class);
    private final JwtTokenService jwtTokenService = mock(JwtTokenService.class);
    private final Clock clock = Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneOffset.UTC);

    private AuthService authService;
    private User user;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userService, otpService, jwtTokenService, clock);
        user = new User();
        user.setEmail("user@example.com");
    }

    @Test
    void requestOtpDelegatesToServices() {
        when(userService.getOrCreate("user@example.com")).thenReturn(user);

        authService.requestOtp(new LoginRequest("user@example.com"));

        verify(userService).getOrCreate("user@example.com");
        verify(otpService).sendOtp("user@example.com");
    }

    @Test
    void confirmOtpReturnsTokens() {
        ConfirmLoginRequest request = new ConfirmLoginRequest("user@example.com", "123456");
        AuthTokens tokens = new AuthTokens("access", Instant.now(), "refresh", Instant.now());
        when(userService.updateLastLogin(eq("user@example.com"), any())).thenReturn(user);
        when(jwtTokenService.generateTokens(user)).thenReturn(tokens);

        AuthTokens result = authService.confirmOtp(request);

        verify(otpService).validateOtp("user@example.com", "123456");
        verify(userService).updateLastLogin("user@example.com", clock.instant());
        assertThat(result).isEqualTo(tokens);
    }
}

