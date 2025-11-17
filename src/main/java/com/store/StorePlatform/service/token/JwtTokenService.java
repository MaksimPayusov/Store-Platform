package com.store.StorePlatform.service.token;

import com.store.StorePlatform.config.properties.JwtProperties;
import com.store.StorePlatform.entity.User;
import com.store.StorePlatform.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtProperties jwtProperties;
    private final Clock clock;

    private SecretKey secretKey;

    @PostConstruct
    void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public AuthTokens generateTokens(User user) {
        Instant now = clock.instant();
        Instant accessExpiresAt = now.plus(jwtProperties.getAccessTokenTtl());
        Instant refreshExpiresAt = now.plus(jwtProperties.getRefreshTokenTtl());

        String accessToken = createToken(user, TokenType.ACCESS, now, accessExpiresAt);
        String refreshToken = createToken(user, TokenType.REFRESH, now, refreshExpiresAt);

        return new AuthTokens(accessToken, accessExpiresAt, refreshToken, refreshExpiresAt);
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String createToken(User user, TokenType type, Instant issuedAt, Instant expiresAt) {
        Map<String, Object> claims = Map.of(
                "roles", user.getRoles().stream().map(UserRole::name).collect(Collectors.toSet()),
                "type", type.name()
        );

        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(user.getEmail())
                .claims(claims)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey)
                .compact();
    }

    public boolean isRefreshToken(Claims claims) {
        return TokenType.REFRESH.name().equals(claims.get("type"));
    }
}

