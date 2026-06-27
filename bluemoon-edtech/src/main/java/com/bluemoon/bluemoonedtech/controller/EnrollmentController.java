package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.dto.CourseResponseDTO;
import com.bluemoon.bluemoonedtech.security.CustomUserDetails;
import com.bluemoon.bluemoonedtech.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/courses/{courseId}/request-access")
    public ResponseEntity<?> requestAccess(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long courseId
    ) {
        enrollmentService.requestAccess(user.getId(), courseId);
        return ResponseEntity.ok("Access request sent");
    }

    @GetMapping("/my-courses")
    public ResponseEntity<List<CourseResponseDTO>> getMyCourses(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(enrollmentService.getMyCourses(user.getId()));
    }
}
