package com.store.StorePlatform.service.token;

import com.store.StorePlatform.config.properties.JwtProperties;
import com.store.StorePlatform.entity.User;
import com.store.StorePlatform.entity.UserRole;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenServiceTest {

    private JwtTokenService tokenService;
    private final JwtProperties properties = new JwtProperties();
    private final Clock clock = Clock.systemUTC();

    @BeforeEach
    void setUp() {
        properties.setSecret("change-me-change-me-change-me-change-me!");
        properties.setAccessTokenTtl(Duration.ofMinutes(15));
        properties.setRefreshTokenTtl(Duration.ofDays(7));
        tokenService = new JwtTokenService(properties, clock);
        tokenService.init();
    }

    @Test
    void generateAndParseTokens() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setRoles(Set.of(UserRole.CUSTOMER));

        AuthTokens tokens = tokenService.generateTokens(user);

        Claims claims = tokenService.parseToken(tokens.accessToken());
        assertThat(claims.getSubject()).isEqualTo("user@example.com");
        assertThat(tokenService.isRefreshToken(claims)).isFalse();

        Claims refreshClaims = tokenService.parseToken(tokens.refreshToken());
        assertThat(tokenService.isRefreshToken(refreshClaims)).isTrue();
    }
}

