package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.dto.AddLessonRequest;
import com.bluemoon.bluemoonedtech.dto.LessonResponseDTO;
import com.bluemoon.bluemoonedtech.dto.UpdateLessonRequest;
import com.bluemoon.bluemoonedtech.entity.Course;
import com.bluemoon.bluemoonedtech.entity.Lesson;
import com.bluemoon.bluemoonedtech.exception.ConflictException;
import com.bluemoon.bluemoonedtech.exception.ResourceNotFoundException;
import com.bluemoon.bluemoonedtech.repository.CourseRepository;
import com.bluemoon.bluemoonedtech.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    // Add Lesson
    public LessonResponseDTO addLesson(Long courseId, AddLessonRequest request) {

        if (request.getOrderIndex() <= 0) {
            throw new IllegalArgumentException("orderIndex must be >= 1");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        boolean orderIndexExists =
                lessonRepository.existsByCourseIdAndOrderIndex(
                        courseId, request.getOrderIndex());

        if (orderIndexExists) {
            throw new ConflictException(
                    "Lesson with orderIndex " + request.getOrderIndex()
                            + " already exists in this course");
        }

        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .videoUrl(request.getVideoUrl())
                .orderIndex(request.getOrderIndex())
                .course(course)
                .build();

        Lesson saved = lessonRepository.save(lesson);
        return mapToDTO(saved);
    }

    // Delete Lesson
    public void deleteLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        lessonRepository.delete(lesson);
    }

    // Update Lesson
    public LessonResponseDTO updateLesson(Long lessonId, UpdateLessonRequest request) {

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        if (request.getOrderIndex() != null) {

            if (request.getOrderIndex() <= 0) {
                throw new IllegalArgumentException("orderIndex must be >= 1");
            }

            boolean exists =
                    lessonRepository.existsByCourseIdAndOrderIndex(
                            lesson.getCourse().getId(),
                            request.getOrderIndex()
                    );

            if (exists && request.getOrderIndex() != lesson.getOrderIndex()) {
                throw new ConflictException(
                        "Lesson with this orderIndex already exists in this course"
                );
            }

            lesson.setOrderIndex(request.getOrderIndex());
        }

        if (request.getTitle() != null) {
            lesson.setTitle(request.getTitle());
        }

        if (request.getVideoUrl() != null) {
            lesson.setVideoUrl(request.getVideoUrl());
        }

        Lesson updated = lessonRepository.save(lesson);
        return mapToDTO(updated);
    }

    // Get lessons by course
    public List<LessonResponseDTO> getLessonsByCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found");
        }

        List<Lesson> lessons = lessonRepository
                .findByCourseIdOrderByOrderIndexAsc(courseId);

        return lessons.stream()
                .map(this::mapToDTO)
                .toList();
    }

    // Mapping method
    private LessonResponseDTO mapToDTO(Lesson lesson) {
        return LessonResponseDTO.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .videoUrl(lesson.getVideoUrl())
                .orderIndex(lesson.getOrderIndex())
                .build();
    }
}
