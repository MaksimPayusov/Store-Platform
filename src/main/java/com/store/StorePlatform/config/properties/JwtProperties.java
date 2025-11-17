package com.store.StorePlatform.config.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    /**
     * Shared secret used for signing JWT tokens.
     */
    @NotBlank
    private String secret;

    /**
     * Issuer claim value for generated tokens.
     */
    @NotBlank
    private String issuer = "store-platform";

    /**
     * Access token lifetime.
     */
    private Duration accessTokenTtl = Duration.ofMinutes(15);

    /**
     * Refresh token lifetime.
     */
    private Duration refreshTokenTtl = Duration.ofDays(7);

    /**
     * Cookie configuration for refresh token transport.
     */
    private final RefreshCookie cookie = new RefreshCookie();

    @Data
    public static class RefreshCookie {
        private String name = "refresh_token";
        private String path = "/";
        private String domain;
        private boolean secure = false;
        private boolean httpOnly = true;
        private SameSite sameSite = SameSite.LAX;
    }

    public enum SameSite {
        STRICT("Strict"),
        LAX("Lax"),
        NONE("None");

        private final String attribute;

        SameSite(String attribute) {
            this.attribute = attribute;
        }

        public String getAttribute() {
            return attribute;
        }
    }
}

