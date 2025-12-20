package com.bluemoon.bluemoonedtech.otp.repository;

import com.bluemoon.bluemoonedtech.otp.entity.OtpVerification;
import com.bluemoon.bluemoonedtech.otp.enums.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository
        extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification>
    findTopByIdentifierAndPurposeOrderByCreatedAtDesc(
            String identifier,
            OtpPurpose purpose
    );
}
