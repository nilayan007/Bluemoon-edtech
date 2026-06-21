package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.dto.LessonResponseDTO;
import com.bluemoon.bluemoonedtech.security.CustomUserDetails;
import com.bluemoon.bluemoonedtech.service.PublicLessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class PublicLessonController {

    private final PublicLessonService publicLessonService;

    @GetMapping("/{courseId}/lessons")
    public ResponseEntity<List<LessonResponseDTO>> getLessons(
            @PathVariable Long courseId,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(
                publicLessonService.getLessonsByCourse(courseId, user.getId())
        );
    }
}
