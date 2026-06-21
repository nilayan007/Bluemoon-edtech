package com.bluemoon.bluemoonedtech.service;
import com.bluemoon.bluemoonedtech.exception.ResourceNotFoundException;
import com.bluemoon.bluemoonedtech.exception.ConflictException;
import com.bluemoon.bluemoonedtech.exception.InvalidOtpException;
import com.bluemoon.bluemoonedtech.otp.entity.OtpVerification;
import com.bluemoon.bluemoonedtech.otp.enums.OtpPurpose;
import com.bluemoon.bluemoonedtech.otp.repository.OtpVerificationRepository;
import com.bluemoon.bluemoonedtech.otp.service.OtpService;
import com.bluemoon.bluemoonedtech.refresh.service.RefreshTokenService;
import com.bluemoon.bluemoonedtech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bluemoon.bluemoonedtech.email.EmailService;
import com.bluemoon.bluemoonedtech.entity.User;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailChangeService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final EmailService emailService;
    private final RefreshTokenService refreshTokenService;
    private final OtpVerificationRepository otpRepository;


    public void requestEmailChange(Long userId, String newEmail) {

        log.info("Email change request initiated");

        if (userRepository.existsByEmail(newEmail)) {
            log.warn("Email change failed: email already in use");
            throw new ConflictException("Email already in use");
        }

        // 2. Generate OTP for EMAIL_CHANGE
        String otp = otpService.generateOtp(
                newEmail,
                OtpPurpose.EMAIL_CHANGE
        );

        // 3. Send OTP to NEW email (async)
        emailService.sendOtp(newEmail, otp);
        System.out.println("chnage email OTP is" + otp);
        log.info("Email change OTP sent successfully");

    }
    @Transactional
    public void verifyEmailChangeOtp(Long userId, String newEmail, String otp) {
        log.info("Verifying email change OTP");

        otpService.verifyOtp(
                newEmail,
                otp,
                OtpPurpose.EMAIL_CHANGE
        );
        log.info("Email change OTP verified successfully");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        log.info("User is successfully verified");

        log.info("Confirming email change");
        OtpVerification otp1 = otpRepository
                .findTopByIdentifierAndPurposeOrderByCreatedAtDesc(
                        newEmail,
                        OtpPurpose.EMAIL_CHANGE
                )
                .orElseThrow(() -> new InvalidOtpException("OTP not verified"));

        // Update email

        user.setEmail(newEmail);

        userRepository.save(user);
        refreshTokenService.revokeAllForUser(user);
        otpRepository.delete(otp1);

        log.info("Email change confirmed and user sessions revoked");



    }




}

