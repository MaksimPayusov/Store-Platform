package com.store.StorePlatform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Авторизационный токен")
public record AuthResponse(
        @Schema(description = "JWT access токен", example = "eyJhbGciOiJIUzI1NiJ9...")
        String accessToken,
        @Schema(description = "Тип токена", example = "Bearer")
        String tokenType,
        @Schema(description = "Время жизни access токена в секундах", example = "900")
        long expiresIn
) {
}

