package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.entity.Course;
import com.bluemoon.bluemoonedtech.security.CustomUserDetails;
import com.bluemoon.bluemoonedtech.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // 🔐 Get current user ID from JWT
    private Long getCurrentUserId() {
        return ((CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getId();
    }

    // Request access
    @PostMapping("/courses/{courseId}/request-access")
    public ResponseEntity<?> requestAccess(@PathVariable Long courseId) {

        Long userId = getCurrentUserId();

        enrollmentService.requestAccess(userId, courseId);

        return ResponseEntity.ok("Access request sent");
    }

    // My Courses
    @GetMapping("/my-courses")
    public ResponseEntity<List<Course>> getMyCourses() {

        Long userId = getCurrentUserId();

        return ResponseEntity.ok(
                enrollmentService.getMyCourses(userId)
        );
    }
}