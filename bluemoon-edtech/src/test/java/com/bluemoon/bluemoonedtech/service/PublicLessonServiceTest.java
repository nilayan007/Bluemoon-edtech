package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.entity.Course;
import com.bluemoon.bluemoonedtech.entity.Lesson;
import com.bluemoon.bluemoonedtech.exception.ForbiddenException;
import com.bluemoon.bluemoonedtech.repository.CourseRepository;
import com.bluemoon.bluemoonedtech.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PublicLessonServiceTest {

    private LessonRepository lessonRepository;
    private CourseRepository courseRepository;
    private EnrollmentService enrollmentService;
    private PublicLessonService service;

    @BeforeEach
    void setUp() {
        lessonRepository = mock(LessonRepository.class);
        courseRepository = mock(CourseRepository.class);
        enrollmentService = mock(EnrollmentService.class);
        service = new PublicLessonService(
                lessonRepository,
                courseRepository,
                enrollmentService
        );
    }

    @Test
    void rejectsUsersWithoutActiveEnrollment() {
        Course course = Course.builder().id(10L).published(true).build();
        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(enrollmentService.hasActiveEnrollment(5L, 10L)).thenReturn(false);

        assertThrows(
                ForbiddenException.class,
                () -> service.getLessonsByCourse(10L, 5L)
        );
        verifyNoInteractions(lessonRepository);
    }

    @Test
    void returnsLessonsForActivelyEnrolledUsers() {
        Course course = Course.builder().id(10L).published(true).build();
        Lesson lesson = Lesson.builder()
                .id(2L)
                .title("Lesson")
                .videoUrl("https://video.example/lesson")
                .orderIndex(1)
                .course(course)
                .build();
        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(enrollmentService.hasActiveEnrollment(5L, 10L)).thenReturn(true);
        when(lessonRepository.findByCourseIdOrderByOrderIndexAsc(10L))
                .thenReturn(List.of(lesson));

        var result = service.getLessonsByCourse(10L, 5L);

        assertEquals(1, result.size());
        assertEquals("https://video.example/lesson", result.get(0).getVideoUrl());
    }
}
