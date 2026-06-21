package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.entity.Course;
import com.bluemoon.bluemoonedtech.entity.Lesson;
import com.bluemoon.bluemoonedtech.repository.CourseRepository;
import com.bluemoon.bluemoonedtech.repository.LessonRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LessonServiceTest {

    @Test
    void adminLessonListingDoesNotRequireStudentEnrollment() {
        CourseRepository courseRepository = mock(CourseRepository.class);
        LessonRepository lessonRepository = mock(LessonRepository.class);
        LessonService service = new LessonService(courseRepository, lessonRepository);
        Course course = Course.builder().id(8L).build();
        Lesson lesson = Lesson.builder()
                .id(1L)
                .title("Admin visible")
                .videoUrl("video")
                .orderIndex(1)
                .course(course)
                .build();
        when(courseRepository.existsById(8L)).thenReturn(true);
        when(lessonRepository.findByCourseIdOrderByOrderIndexAsc(8L))
                .thenReturn(List.of(lesson));

        var result = service.getLessonsByCourse(8L);

        assertEquals(1, result.size());
        assertEquals("Admin visible", result.get(0).getTitle());
    }
}
