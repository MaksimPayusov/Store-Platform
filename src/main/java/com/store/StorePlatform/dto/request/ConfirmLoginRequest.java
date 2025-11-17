package com.store.StorePlatform.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ConfirmLoginRequest(
        @Email
        @NotBlank
        String email,

        @NotBlank
        @Pattern(regexp = "\\d{4,10}")
        String otp
) {
}

