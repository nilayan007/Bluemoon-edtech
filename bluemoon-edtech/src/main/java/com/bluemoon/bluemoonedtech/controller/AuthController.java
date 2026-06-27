package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.dto.*;
import com.bluemoon.bluemoonedtech.refresh.dto.LogoutRequest;
import com.bluemoon.bluemoonedtech.refresh.dto.RefreshTokenRequest;
import com.bluemoon.bluemoonedtech.service.AuthService;
import com.bluemoon.bluemoonedtech.service.ForgotPasswordService;
import com.bluemoon.bluemoonedtech.service.UserService;
import jakarta.validation.Valid;
import com.bluemoon.bluemoonedtech.dto.AccessTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String REFRESH_TOKEN_COOKIE = "refreshToken";
    private static final Duration REFRESH_TOKEN_COOKIE_MAX_AGE = Duration.ofDays(7);

    private final UserService userService;
    private final AuthService authService;
    private final ForgotPasswordService forgotPasswordService;


    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = userService.register(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response
    ) {

        LoginResponse loginResponse = authService.login(request);
        addRefreshTokenCookie(response, loginResponse.getRefreshToken(), httpRequest.isSecure());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        forgotPasswordService.requestOtp(request.getEmail());

        return ResponseEntity.ok(
                "If the email exists, an OTP has been sent"
        );
    }
    @PostMapping("/verify-forgot-otp")
    public ResponseEntity<String> verifyForgotOtp(
            @Valid @RequestBody VerifyForgotOtpRequest request
    ) {
        forgotPasswordService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );

        return ResponseEntity.ok("OTP verified successfully");
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        forgotPasswordService.resetPassword(
                request.getEmail(),
                request.getNewPassword()
        );

        return ResponseEntity.ok("Password reset successfully, Please login again");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refresh(
            @CookieValue(value = REFRESH_TOKEN_COOKIE, required = false) String refreshTokenCookie,
            @RequestBody(required = false) RefreshTokenRequest request
    ) {
        String refreshToken = resolveRefreshToken(refreshTokenCookie,
                request != null ? request.getRefreshToken() : null);
        return ResponseEntity.ok(
                authService.refresh(refreshToken)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(value = REFRESH_TOKEN_COOKIE, required = false) String refreshTokenCookie,
            @RequestBody(required = false) LogoutRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response
    ) {
        String refreshToken = resolveRefreshToken(refreshTokenCookie,
                request != null ? request.getRefreshToken() : null);
        authService.logout(refreshToken);
        clearRefreshTokenCookie(response, httpRequest.isSecure());
        return ResponseEntity.ok().build();
    }

    private String resolveRefreshToken(String cookieToken, String bodyToken) {
        if (StringUtils.hasText(cookieToken)) {
            return cookieToken;
        }
        if (StringUtils.hasText(bodyToken)) {
            return bodyToken;
        }
        throw new IllegalArgumentException("Refresh token is required");
    }

    private void addRefreshTokenCookie(
            HttpServletResponse response,
            String refreshToken,
            boolean secure
    ) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, refreshToken)
                .httpOnly(true)
                .secure(secure)
                .path("/api/auth")
                .sameSite("Lax")
                .maxAge(REFRESH_TOKEN_COOKIE_MAX_AGE)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearRefreshTokenCookie(HttpServletResponse response, boolean secure) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(secure)
                .path("/api/auth")
                .sameSite("Lax")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }



}
