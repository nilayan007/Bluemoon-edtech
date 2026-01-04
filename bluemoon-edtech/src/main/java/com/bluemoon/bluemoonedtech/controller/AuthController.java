package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.dto.*;
import com.bluemoon.bluemoonedtech.refresh.dto.LogoutRequest;
import com.bluemoon.bluemoonedtech.refresh.dto.RefreshTokenRequest;
import com.bluemoon.bluemoonedtech.refresh.entity.RefreshToken;
import com.bluemoon.bluemoonedtech.refresh.service.RefreshTokenService;
import com.bluemoon.bluemoonedtech.service.AuthService;
import com.bluemoon.bluemoonedtech.service.ForgotPasswordService;
import com.bluemoon.bluemoonedtech.service.UserService;
import jakarta.validation.Valid;
import com.bluemoon.bluemoonedtech.dto.AccessTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.AuthProvider;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final ForgotPasswordService forgotPasswordService;
   // private final RefreshTokenService refreshTokenService;


    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = userService.register(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
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

        return ResponseEntity.ok("Password reset successful");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(
                authService.refresh(request.getRefreshToken())
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody LogoutRequest request
    ) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok().build();
    }




}
