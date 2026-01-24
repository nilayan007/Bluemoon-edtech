package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.dto.AddLessonRequest;
import com.bluemoon.bluemoonedtech.dto.UpdateLessonRequest;
import com.bluemoon.bluemoonedtech.entity.Lesson;
import com.bluemoon.bluemoonedtech.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminLessonController {

    private final LessonService lessonService;

    @PostMapping("/courses/{courseId}/lessons")
    public ResponseEntity<Lesson> addLesson(
            @PathVariable Long courseId,
            @RequestBody AddLessonRequest request) {
        return ResponseEntity.ok(lessonService.addLesson(courseId, request));
    }

    @DeleteMapping("/lessons/{lessonId}")
    public ResponseEntity<Void> deleteLesson(
            @PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/lessons/{lessonId}")
    public ResponseEntity<Lesson> updateLesson(
            @PathVariable Long lessonId,
            @RequestBody UpdateLessonRequest request) {
        return ResponseEntity.ok(lessonService.updateLesson(lessonId, request));
    }

}
