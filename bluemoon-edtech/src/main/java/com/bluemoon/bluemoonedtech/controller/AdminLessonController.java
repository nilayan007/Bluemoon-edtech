package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.dto.AddLessonRequest;
import com.bluemoon.bluemoonedtech.dto.LessonResponseDTO;
import com.bluemoon.bluemoonedtech.dto.UpdateLessonRequest;
import com.bluemoon.bluemoonedtech.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminLessonController {

    private final LessonService lessonService;

    // Add Lesson
    @PostMapping("/courses/{courseId}/lessons")
    public ResponseEntity<LessonResponseDTO> addLesson(
            @PathVariable Long courseId,
            @Valid @RequestBody AddLessonRequest request) {
        return ResponseEntity.ok(lessonService.addLesson(courseId, request));
    }

    // Get lessons by course (ADMIN)
    @GetMapping("/courses/{courseId}/lessons")
    public ResponseEntity<List<LessonResponseDTO>> getLessonsByCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(lessonService.getLessonsByCourse(courseId));
    }

    // Delete Lesson
    @DeleteMapping("/lessons/{lessonId}")
    public ResponseEntity<Void> deleteLesson(
            @PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }

    // Update Lesson
    @PutMapping("/lessons/{lessonId}")
    public ResponseEntity<LessonResponseDTO> updateLesson(
            @PathVariable Long lessonId,
            @Valid @RequestBody UpdateLessonRequest request) {
        return ResponseEntity.ok(lessonService.updateLesson(lessonId, request));
    }
}
