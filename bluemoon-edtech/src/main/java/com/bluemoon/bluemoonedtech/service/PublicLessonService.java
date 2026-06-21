package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.dto.LessonResponseDTO;
import com.bluemoon.bluemoonedtech.entity.Course;
import com.bluemoon.bluemoonedtech.exception.ForbiddenException;
import com.bluemoon.bluemoonedtech.exception.ResourceNotFoundException;
import com.bluemoon.bluemoonedtech.repository.CourseRepository;
import com.bluemoon.bluemoonedtech.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicLessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentService enrollmentService;

    public List<LessonResponseDTO> getLessonsByCourse(Long courseId, Long userId) {

        // 1 Fetch course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // 2️ Check published status
        if (!course.isPublished()) {
            throw new ResourceNotFoundException("Course not found");
        }

        if (!enrollmentService.hasActiveEnrollment(userId, courseId)) {
            throw new ForbiddenException("Active enrollment required");
        }

        // 3️ Fetch lessons only if course is published
        return lessonRepository.findByCourseIdOrderByOrderIndexAsc(courseId)
                .stream()
                .map(lesson -> LessonResponseDTO.builder()
                        .id(lesson.getId())
                        .title(lesson.getTitle())
                        .videoUrl(lesson.getVideoUrl())
                        .orderIndex(lesson.getOrderIndex())
                        .build())
                .toList();
    }
}
