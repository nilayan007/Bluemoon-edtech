package com.bluemoon.bluemoonedtech.service;

//package com.bluemoon.bluemoonedtech.auth.service;
import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.exception.ResourceNotFoundException;
import com.bluemoon.bluemoonedtech.otp.entity.OtpVerification;
import com.bluemoon.bluemoonedtech.otp.repository.OtpVerificationRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bluemoon.bluemoonedtech.otp.enums.OtpPurpose;
import com.bluemoon.bluemoonedtech.otp.service.OtpService;
import com.bluemoon.bluemoonedtech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bluemoon.bluemoonedtech.email.EmailService;
import com.bluemoon.bluemoonedtech.refresh.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final OtpVerificationRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService ;
    private final RefreshTokenService refreshTokenService;

    public void requestOtp(String email) {
        log.info("Forgot password OTP request received");

        // SECURITY: do not reveal user existence
        userRepository.findByEmail(email).ifPresent(user -> {
            String otp = otpService.generateOtp(
                    email,
                    OtpPurpose.FORGOT_PASSWORD
            );




            emailService.sendOtp(email, otp);

            System.out.println("Forgot Password OTP for " + email + " = " + otp);
            log.info("Forgot password OTP sent successfully");
        });

        // Always return success
    }
    public void verifyOtp(String email, String otp) {
        log.info("Verifying forgot password OTP");
        otpService.verifyOtp(
                email,
                otp,
                OtpPurpose.FORGOT_PASSWORD
        );
        log.info("Forgot password OTP verified successfully");
    }
    public void resetPassword(String email, String newPassword) {

        // ensure OTP was verified
        OtpVerification otp = otpRepository
                .findTopByIdentifierAndPurposeOrderByCreatedAtDesc(
                        email,
                        OtpPurpose.FORGOT_PASSWORD
                )
                .orElseThrow(() -> new RuntimeException("OTP not verified"));

        if (!otp.isUsed()) {
            log.warn("Reset password failed: OTP not verified");
            throw new RuntimeException("OTP not verified");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // cleanup OTP
        otpRepository.delete(otp);

        // OPTIONAL (add later)
        // jwtInvalidationService.invalidateUserTokens(user.getId());
        refreshTokenService.revokeAllForUser(user);
        log.info("Password reset successfully and user sessions revoked");

    }

}

