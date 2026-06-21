package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.dto.CourseResponseDTO;
import com.bluemoon.bluemoonedtech.dto.EnrollmentResponseDTO;
import com.bluemoon.bluemoonedtech.entity.Course;
import com.bluemoon.bluemoonedtech.entity.Enrollment;
import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.enums.EnrollmentStatus;
import com.bluemoon.bluemoonedtech.exception.ConflictException;
import com.bluemoon.bluemoonedtech.exception.ResourceNotFoundException;
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
            throw new ConflictException("Already requested or enrolled");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Course course = courseRepository.findById(courseId)
                .filter(Course::isPublished)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

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
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        if (enrollment.getStatus() != EnrollmentStatus.PENDING) {
            throw new ConflictException("Only pending enrollments can be approved");
        }

        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setStartDate(LocalDateTime.now());
        enrollment.setExpiryDate(LocalDateTime.now().plusDays(30));
    }

    // My Courses (ACTIVE only)
    public List<CourseResponseDTO> getMyCourses(Long userId) {
        return enrollmentRepository
                .findByUserIdAndStatus(userId, EnrollmentStatus.ACTIVE)
                .stream()
                .filter(e -> e.getExpiryDate() != null
                        && e.getExpiryDate().isAfter(LocalDateTime.now()))
                .map(Enrollment::getCourse)
                .map(this::mapCourse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getPendingEnrollments() {
        return enrollmentRepository.findByStatus(EnrollmentStatus.PENDING)
                .stream()
                .map(this::mapEnrollment)
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
                                e.getExpiryDate() != null &&
                                e.getExpiryDate().isAfter(LocalDateTime.now())
                )
                .orElse(false);
    }

    private CourseResponseDTO mapCourse(Course course) {
        return CourseResponseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .thumbnailUrl(course.getThumbnailUrl())
                .published(course.isPublished())
                .build();
    }

    private EnrollmentResponseDTO mapEnrollment(Enrollment enrollment) {
        return EnrollmentResponseDTO.builder()
                .id(enrollment.getId())
                .userId(enrollment.getUser().getPublicId())
                .userName(enrollment.getUser().getName())
                .userEmail(enrollment.getUser().getEmail())
                .courseId(enrollment.getCourse().getId())
                .courseTitle(enrollment.getCourse().getTitle())
                .status(enrollment.getStatus())
                .startDate(enrollment.getStartDate())
                .expiryDate(enrollment.getExpiryDate())
                .build();
    }
}
