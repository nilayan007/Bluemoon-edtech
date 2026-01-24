package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.dto.CourseResponseDTO;
import com.bluemoon.bluemoonedtech.entity.Course;
import com.bluemoon.bluemoonedtech.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCourseService {

    private final CourseRepository courseRepository;

    public List<CourseResponseDTO> getAllPublishedCourses() {
        return courseRepository.findAll()
                .stream()
                .filter(Course::isPublished)
                .map(course -> CourseResponseDTO.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .description(course.getDescription())
                        .thumbnailUrl(course.getThumbnailUrl())
                        .build())
                .toList();
    }

    public CourseResponseDTO getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .filter(Course::isPublished)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return CourseResponseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .thumbnailUrl(course.getThumbnailUrl())
                .build();
    }
}
