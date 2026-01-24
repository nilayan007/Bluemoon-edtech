package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.dto.AddLessonRequest;
import com.bluemoon.bluemoonedtech.dto.UpdateLessonRequest;
import com.bluemoon.bluemoonedtech.entity.Course;
import com.bluemoon.bluemoonedtech.entity.Lesson;
import com.bluemoon.bluemoonedtech.repository.CourseRepository;
import com.bluemoon.bluemoonedtech.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public Lesson addLesson(Long courseId, AddLessonRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .videoUrl(request.getVideoUrl())
                .orderIndex(request.getOrderIndex())
                .course(course)
                .build();

        return lessonRepository.save(lesson);
    }

    public void deleteLesson(Long lessonId) {
        lessonRepository.deleteById(lessonId);
    }
    public Lesson updateLesson(Long lessonId, UpdateLessonRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        if (request.getTitle() != null) {
            lesson.setTitle(request.getTitle());
        }
        if (request.getVideoUrl() != null) {
            lesson.setVideoUrl(request.getVideoUrl());
        }
        if (request.getOrderIndex() != null) {
            lesson.setOrderIndex(request.getOrderIndex());
        }

        return lessonRepository.save(lesson);
    }
}
