package com.store.StorePlatform;

import com.store.StorePlatform.config.properties.JwtProperties;
import com.store.StorePlatform.config.properties.OtpProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.security.SecureRandom;
import java.time.Clock;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, OtpProperties.class})
public class StorePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorePlatformApplication.class, args);
	}

	@Bean
	public Clock systemClock() {
		return Clock.systemUTC();
	}

	@Bean
	public SecureRandom secureRandom() {
		return new SecureRandom();
	}
}
