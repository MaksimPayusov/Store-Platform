package com.store.StorePlatform.config.properties;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "security.otp")
public class OtpProperties {

    /**
     * Length of the generated OTP code.
     */
    @Min(4)
    private int length = 6;

    /**
     * Expiration time for OTP codes.
     */
    private Duration ttl = Duration.ofMinutes(5);

    /**
     * Maximum number of resend attempts within the TTL.
     */
    @Min(1)
    private int resendLimit = 3;
}

