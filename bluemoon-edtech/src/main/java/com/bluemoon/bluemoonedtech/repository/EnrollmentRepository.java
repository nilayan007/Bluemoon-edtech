package com.bluemoon.bluemoonedtech.repository;

import com.bluemoon.bluemoonedtech.entity.Enrollment;
import com.bluemoon.bluemoonedtech.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByUserIdAndStatus(Long userId, EnrollmentStatus status);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    List<Enrollment> findByStatusAndExpiryDateBefore(
            EnrollmentStatus status,
            java.time.LocalDateTime now
    );

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
    Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);
}