package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.dto.CreateCourseRequest;
import com.bluemoon.bluemoonedtech.dto.UpdateCourseRequest;
import com.bluemoon.bluemoonedtech.entity.Course;
import com.bluemoon.bluemoonedtech.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<Course> createCourse(
            @RequestBody CreateCourseRequest request) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{courseId}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long courseId,
            @RequestBody UpdateCourseRequest request) {
        return ResponseEntity.ok(courseService.updateCourse(courseId, request));
    }

}
