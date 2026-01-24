package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.dto.CourseResponseDTO;
import com.bluemoon.bluemoonedtech.service.PublicCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class PublicCourseController {

    private final PublicCourseService publicCourseService;

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        return ResponseEntity.ok(publicCourseService.getAllPublishedCourses());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponseDTO> getCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(publicCourseService.getCourseById(courseId));
    }
}
