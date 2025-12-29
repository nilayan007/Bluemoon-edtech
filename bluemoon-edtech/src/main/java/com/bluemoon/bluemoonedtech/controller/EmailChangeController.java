package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.dto.ConfirmEmailChangeRequest;
import com.bluemoon.bluemoonedtech.dto.RequestEmailChangeRequest;
import com.bluemoon.bluemoonedtech.dto.VerifyEmailChangeOtpRequest;
import com.bluemoon.bluemoonedtech.security.CustomUserDetails;
import com.bluemoon.bluemoonedtech.service.EmailChangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/change-email")
@RequiredArgsConstructor
public class EmailChangeController {

    private final EmailChangeService emailChangeService;

    @PostMapping("/request")
    public ResponseEntity<String> requestEmailChange(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody RequestEmailChangeRequest request
    ) {
        emailChangeService.requestEmailChange(
                user.getId(),
                request.getNewEmail()
        );

        return ResponseEntity.ok("OTP sent to new email");
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyEmailChangeOtp(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody VerifyEmailChangeOtpRequest request
    ) {
        emailChangeService.verifyEmailChangeOtp(
                user.getId(),
                request.getNewEmail(),
                request.getOtp()
        );

        return ResponseEntity.ok("Email changed successfully");
    }
}
