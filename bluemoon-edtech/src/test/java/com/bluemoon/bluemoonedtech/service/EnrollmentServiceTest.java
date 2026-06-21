package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.entity.Course;
import com.bluemoon.bluemoonedtech.entity.Enrollment;
import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.enums.EnrollmentStatus;
import com.bluemoon.bluemoonedtech.exception.ConflictException;
import com.bluemoon.bluemoonedtech.repository.CourseRepository;
import com.bluemoon.bluemoonedtech.repository.EnrollmentRepository;
import com.bluemoon.bluemoonedtech.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    private EnrollmentRepository enrollmentRepository;
    private EnrollmentService service;

    @BeforeEach
    void setUp() {
        enrollmentRepository = mock(EnrollmentRepository.class);
        service = new EnrollmentService(
                enrollmentRepository,
                mock(CourseRepository.class),
                mock(UserRepository.class)
        );
    }

    @Test
    void myCoursesReturnsDtosInsteadOfEntities() {
        Course course = Course.builder()
                .id(7L)
                .title("Java")
                .published(true)
                .build();
        Enrollment enrollment = Enrollment.builder()
                .course(course)
                .status(EnrollmentStatus.ACTIVE)
                .expiryDate(LocalDateTime.now().plusDays(1))
                .build();
        when(enrollmentRepository.findByUserIdAndStatus(3L, EnrollmentStatus.ACTIVE))
                .thenReturn(List.of(enrollment));

        var result = service.getMyCourses(3L);

        assertEquals(1, result.size());
        assertEquals(7L, result.get(0).getId());
        assertTrue(result.get(0).isPublished());
    }

    @Test
    void pendingEnrollmentsAreMappedToSafeDtos() {
        User user = User.builder()
                .publicId("user-public-id")
                .name("Student")
                .email("student@example.com")
                .build();
        Course course = Course.builder().id(4L).title("Spring").build();
        Enrollment enrollment = Enrollment.builder()
                .id(9L)
                .user(user)
                .course(course)
                .status(EnrollmentStatus.PENDING)
                .build();
        when(enrollmentRepository.findByStatus(EnrollmentStatus.PENDING))
                .thenReturn(List.of(enrollment));

        var result = service.getPendingEnrollments();

        assertEquals("user-public-id", result.get(0).getUserId());
        assertEquals("student@example.com", result.get(0).getUserEmail());
        assertEquals("Spring", result.get(0).getCourseTitle());
    }

    @Test
    void cannotApproveEnrollmentTwice() {
        Enrollment enrollment = Enrollment.builder()
                .status(EnrollmentStatus.ACTIVE)
                .build();
        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        assertThrows(ConflictException.class, () -> service.approveEnrollment(1L));
    }
}
