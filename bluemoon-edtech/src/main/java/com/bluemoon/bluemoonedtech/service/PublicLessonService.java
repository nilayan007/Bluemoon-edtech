package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.dto.LessonResponseDTO;
import com.bluemoon.bluemoonedtech.entity.Course;
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

    public List<LessonResponseDTO> getLessonsByCourse(Long courseId) {

        // 1️⃣ Fetch course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // 2️⃣ Check published status
        if (!course.isPublished()) {
            throw new RuntimeException("Course not published");
        }

        // 3️⃣ Fetch lessons only if course is published
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
