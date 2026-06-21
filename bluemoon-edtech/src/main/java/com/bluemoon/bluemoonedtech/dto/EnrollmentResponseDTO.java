package com.bluemoon.bluemoonedtech.dto;

import com.bluemoon.bluemoonedtech.enums.EnrollmentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EnrollmentResponseDTO {
    private Long id;
    private String userId;
    private String userName;
    private String userEmail;
    private Long courseId;
    private String courseTitle;
    private EnrollmentStatus status;
    private LocalDateTime startDate;
    private LocalDateTime expiryDate;
}
