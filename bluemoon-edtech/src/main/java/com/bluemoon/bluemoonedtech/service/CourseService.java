package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.dto.CourseResponseDTO;
import com.bluemoon.bluemoonedtech.dto.CreateCourseRequest;
import com.bluemoon.bluemoonedtech.dto.UpdateCourseRequest;
import com.bluemoon.bluemoonedtech.entity.Course;
import com.bluemoon.bluemoonedtech.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    // Create Course
    public CourseResponseDTO createCourse(CreateCourseRequest request) {
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .thumbnailUrl(request.getThumbnailUrl())
                .published(false)
                .build();

        Course saved = courseRepository.save(course);
        return mapToDTO(saved);
    }

    // et ALL courses (admin)
    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // Get course by ID
    public CourseResponseDTO getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return mapToDTO(course);
    }

    // Update course
    public CourseResponseDTO updateCourse(Long courseId, UpdateCourseRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (request.getTitle() != null) {
            course.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            course.setDescription(request.getDescription());
        }
        if (request.getThumbnailUrl() != null) {
            course.setThumbnailUrl(request.getThumbnailUrl());
        }
        if (request.getPublished() != null) {
            course.setPublished(request.getPublished());
        }

        Course updated = courseRepository.save(course);
        return mapToDTO(updated);
    }

    // Delete course
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        courseRepository.delete(course);
    }

    // Publish
    public CourseResponseDTO publishCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setPublished(true);
        return mapToDTO(courseRepository.save(course));
    }

    // Unpublish
    public CourseResponseDTO unpublishCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setPublished(false);
        return mapToDTO(courseRepository.save(course));
    }

    // Mapping method
    private CourseResponseDTO mapToDTO(Course course) {
        return CourseResponseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .thumbnailUrl(course.getThumbnailUrl())
                .build();
    }
}