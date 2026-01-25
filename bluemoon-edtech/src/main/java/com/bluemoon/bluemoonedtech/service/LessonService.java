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
        if (request.getOrderIndex() <= 0) {
            throw new IllegalArgumentException("orderIndex must be >= 1");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        boolean orderIndexExists =
                lessonRepository.existsByCourseIdAndOrderIndex(
                        courseId, request.getOrderIndex());

        if (orderIndexExists) {
            throw new IllegalArgumentException(
                    "Lesson with orderIndex " + request.getOrderIndex()
                            + " already exists in this course");
        }

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

        // ONLY validate orderIndex IF admin wants to change it
        if (request.getOrderIndex() != null) {

            if (request.getOrderIndex() <= 0) {
                throw new IllegalArgumentException("orderIndex must be >= 1");
            }

            boolean exists =
                    lessonRepository.existsByCourseIdAndOrderIndex(
                            lesson.getCourse().getId(),
                            request.getOrderIndex()
                    );

            // prevent collision with OTHER lessons
            if (exists && request.getOrderIndex() != lesson.getOrderIndex()) {
                throw new IllegalArgumentException(
                        "Lesson with this orderIndex already exists in this course"
                );
            }

            lesson.setOrderIndex(request.getOrderIndex());
        }

        // title update is totally independent
        if (request.getTitle() != null) {
            lesson.setTitle(request.getTitle());
        }

        if (request.getVideoUrl() != null) {
            lesson.setVideoUrl(request.getVideoUrl());
        }

        return lessonRepository.save(lesson);
    }

}
