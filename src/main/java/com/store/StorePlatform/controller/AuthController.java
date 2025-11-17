package com.store.StorePlatform.controller;

import com.store.StorePlatform.config.properties.JwtProperties;
import com.store.StorePlatform.config.properties.JwtProperties.RefreshCookie;
import com.store.StorePlatform.dto.request.ConfirmLoginRequest;
import com.store.StorePlatform.dto.request.LoginRequest;
import com.store.StorePlatform.dto.response.AuthResponse;
import com.store.StorePlatform.service.AuthService;
import com.store.StorePlatform.service.token.AuthTokens;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "AUTH", description = "Модуль авторизации по одноразовым кодам")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @Operation(summary = "Запрос одноразового кода входа")
    @PostMapping({"/login", "/login/"})
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        authService.requestOtp(request);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Подтверждение одноразового кода и получение токена")
    @PostMapping({"/confirm", "/confirm/"})
    public ResponseEntity<AuthResponse> confirm(@Valid @RequestBody ConfirmLoginRequest request,
                                                HttpServletResponse response) {
        AuthTokens tokens = authService.confirmOtp(request);
        writeRefreshCookie(response, tokens);
        AuthResponse body = new AuthResponse(
                tokens.accessToken(),
                "Bearer",
                jwtProperties.getAccessTokenTtl().toSeconds()
        );
        return ResponseEntity.ok(body);
    }

    private void writeRefreshCookie(HttpServletResponse response, AuthTokens tokens) {
        RefreshCookie cookie = jwtProperties.getCookie();
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(cookie.getName(), tokens.refreshToken())
                .httpOnly(cookie.isHttpOnly())
                .secure(cookie.isSecure())
                .path(cookie.getPath())
                .maxAge(jwtProperties.getRefreshTokenTtl());

        if (cookie.getDomain() != null) {
            builder.domain(cookie.getDomain());
        }

        builder.sameSite(cookie.getSameSite().getAttribute());

        response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());
    }
}

