package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.entity.Course;
import com.bluemoon.bluemoonedtech.entity.Enrollment;
import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.enums.EnrollmentStatus;
import com.bluemoon.bluemoonedtech.repository.CourseRepository;
import com.bluemoon.bluemoonedtech.repository.EnrollmentRepository;
import com.bluemoon.bluemoonedtech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    //  User requests access
    public void requestAccess(Long userId, Long courseId) {

        if (enrollmentRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new RuntimeException("Already requested/enrolled");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .status(EnrollmentStatus.PENDING)
                .build();

        enrollmentRepository.save(enrollment);
    }

    //  Admin approves
    public void approveEnrollment(Long enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setStartDate(LocalDateTime.now());
        enrollment.setExpiryDate(LocalDateTime.now().plusDays(30));
    }

    // My Courses (ACTIVE only)
    public List<Course> getMyCourses(Long userId) {
        return enrollmentRepository
                .findByUserIdAndStatus(userId, EnrollmentStatus.ACTIVE)
                .stream()
                .filter(e -> e.getExpiryDate().isAfter(LocalDateTime.now()))
                .map(Enrollment::getCourse)
                .toList();
    }

    // Expire enrollments (used by JOB)
    public void expireEnrollments() {
        List<Enrollment> expired =
                enrollmentRepository.findByStatusAndExpiryDateBefore(
                        EnrollmentStatus.ACTIVE,
                        LocalDateTime.now()
                );

        for (Enrollment e : expired) {
            e.setStatus(EnrollmentStatus.EXPIRED);
        }
    }
    public boolean hasActiveEnrollment(Long userId, Long courseId) {

        return enrollmentRepository
                .findByUserIdAndCourseId(userId, courseId)
                .map(e ->
                        e.getStatus() == EnrollmentStatus.ACTIVE &&
                                e.getExpiryDate().isAfter(LocalDateTime.now())
                )
                .orElse(false);
    }
}