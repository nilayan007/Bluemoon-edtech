package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.dto.CourseResponseDTO;
import com.bluemoon.bluemoonedtech.dto.CreateCourseRequest;
import com.bluemoon.bluemoonedtech.dto.UpdateCourseRequest;
import com.bluemoon.bluemoonedtech.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;

    // Create Course
    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(
            @RequestBody CreateCourseRequest request) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    // Get ALL courses (published and unpublished)
    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    // Get course by ID
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponseDTO> getCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    // Update course
    @PutMapping("/{courseId}")
    public ResponseEntity<CourseResponseDTO> updateCourse(
            @PathVariable Long courseId,
            @RequestBody UpdateCourseRequest request) {
        return ResponseEntity.ok(courseService.updateCourse(courseId, request));
    }

    // Delete course
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    // Publish course
    @PutMapping("/{courseId}/publish")
    public ResponseEntity<CourseResponseDTO> publishCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.publishCourse(courseId));
    }

    // Unpublish course
    @PutMapping("/{courseId}/unpublish")
    public ResponseEntity<CourseResponseDTO> unpublishCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.unpublishCourse(courseId));
    }
}