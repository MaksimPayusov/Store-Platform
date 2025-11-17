package com.store.StorePlatform.service.token;

import java.time.Instant;

public record AuthTokens(
        String accessToken,
        Instant accessTokenExpiresAt,
        String refreshToken,
        Instant refreshTokenExpiresAt
) {
}

