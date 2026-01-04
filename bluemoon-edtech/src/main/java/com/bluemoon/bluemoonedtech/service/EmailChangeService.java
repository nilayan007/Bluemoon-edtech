package com.bluemoon.bluemoonedtech.service;
import com.bluemoon.bluemoonedtech.otp.enums.OtpPurpose;
import com.bluemoon.bluemoonedtech.otp.service.OtpService;
import com.bluemoon.bluemoonedtech.refresh.service.RefreshTokenService;
import com.bluemoon.bluemoonedtech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bluemoon.bluemoonedtech.email.EmailService;
import com.bluemoon.bluemoonedtech.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailChangeService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final EmailService emailService;
    private final RefreshTokenService refreshTokenService;


    public void requestEmailChange(Long userId, String newEmail) {

        // 1. Check if new email already exists
        if (userRepository.existsByEmail(newEmail)) {
            throw new RuntimeException("Email already in use");
        }

        // 2. Generate OTP for EMAIL_CHANGE
        String otp = otpService.generateOtp(
                newEmail,
                OtpPurpose.EMAIL_CHANGE
        );

        // 3. Send OTP to NEW email (async)
        emailService.sendOtp(newEmail, otp);
        System.out.println("chnage email OTP is" + otp);

    }
    @Transactional
    public void verifyEmailChangeOtp(Long userId, String newEmail, String otp) {

        otpService.verifyOtp(
                newEmail,
                otp,
                OtpPurpose.EMAIL_CHANGE
        );
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(newEmail);
        userRepository.save(user);

    }

    public void confirmEmailChange(Long userId, String newEmail) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update email
        user.setEmail(newEmail);

        userRepository.save(user);
        refreshTokenService.revokeAllForUser(user);



    }


}

