package com.bluemoon.bluemoonedtech.otp.service;

import com.bluemoon.bluemoonedtech.exception.InvalidOtpException;
import com.bluemoon.bluemoonedtech.otp.entity.OtpVerification;
import com.bluemoon.bluemoonedtech.otp.enums.OtpPurpose;
import com.bluemoon.bluemoonedtech.otp.repository.OtpVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpVerificationRepository otpRepository;
    private final PasswordEncoder passwordEncoder;

    public String generateOtp(String identifier, OtpPurpose purpose) {
        log.info("OTP generation started for purpose {}", purpose);
        String otp = String.format("%06d", new Random().nextInt(999999));

        OtpVerification otpEntity = OtpVerification.builder()
                .identifier(identifier)
                .otpHash(passwordEncoder.encode(otp))
                .purpose(purpose)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .createdAt(LocalDateTime.now())
                .build();

        otpRepository.save(otpEntity);
        log.info("OTP generated successfully for purpose {}", purpose);
        return otp; // send via email later
    }

    public void verifyOtp(String identifier, String otp, OtpPurpose purpose) {
        log.info("Verifying OTP for purpose {}", purpose);
        OtpVerification otpEntity = otpRepository
                .findTopByIdentifierAndPurposeOrderByCreatedAtDesc(identifier, purpose)
                .orElseThrow(() -> {
                    log.warn("OTP verification failed: OTP not found");
                    return new InvalidOtpException("OTP not found");
                });

        if (otpEntity.isUsed()) {
            log.warn("OTP verification failed: OTP already used");
            throw new InvalidOtpException("OTP already used");
        }

        if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
            log.warn("OTP verification failed: OTP expired");
            throw new InvalidOtpException("OTP expired");
        }

        if (!passwordEncoder.matches(otp, otpEntity.getOtpHash())) {
            log.warn("OTP verification failed: OTP mismatch");
            throw new InvalidOtpException("Invalid OTP");
        }

        otpEntity.setUsed(true);
        otpRepository.save(otpEntity);
        log.info("OTP verified successfully for purpose {}", purpose);
    }
}
