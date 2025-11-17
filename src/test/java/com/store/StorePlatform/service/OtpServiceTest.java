package com.store.StorePlatform.service;

import com.store.StorePlatform.config.properties.OtpProperties;
import com.store.StorePlatform.exception.InvalidOtpException;
import com.store.StorePlatform.service.notification.OtpNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class OtpServiceTest {

    private final OtpStore otpStore = mock(OtpStore.class);
    private final OtpNotificationService notificationService = mock(OtpNotificationService.class);
    private final java.security.SecureRandom secureRandom = new java.security.SecureRandom(new byte[]{1,2,3,4});
    private final OtpProperties properties = new OtpProperties();

    private OtpService otpService;

    @BeforeEach
    void setUp() {
        properties.setLength(6);
        otpService = new OtpService(otpStore, properties, notificationService, secureRandom);
    }

    @Test
    void sendOtpGeneratesAndStoresCode() {
        ArgumentCaptor<String> otpCaptor = ArgumentCaptor.forClass(String.class);

        otpService.sendOtp("user@example.com");

        verify(otpStore).saveOtp(eq("user@example.com"), otpCaptor.capture(), any());
        verify(notificationService).sendOtp(eq("user@example.com"), otpCaptor.getValue());
        assertThat(otpCaptor.getValue()).hasSize(6).containsOnlyDigits();
    }

    @Test
    void validateOtpDeletesCodeWhenMatches() {
        when(otpStore.getOtp("user@example.com")).thenReturn("123456");

        otpService.validateOtp("user@example.com", "123456");

        verify(otpStore).deleteOtp("user@example.com");
    }

    @Test
    void validateOtpThrowsWhenInvalid() {
        when(otpStore.getOtp("user@example.com")).thenReturn(null);

        assertThatThrownBy(() -> otpService.validateOtp("user@example.com", "123456"))
                .isInstanceOf(InvalidOtpException.class);
    }
}

