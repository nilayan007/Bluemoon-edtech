package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.dto.LessonResponseDTO;
import com.bluemoon.bluemoonedtech.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicLessonService {

    private final LessonRepository lessonRepository;

    public List<LessonResponseDTO> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseIdOrderByOrderIndexAsc(courseId)
                .stream()
                .map(lesson -> LessonResponseDTO.builder()
                        .id(lesson.getId())
                        .title(lesson.getTitle())
                        .videoUrl(lesson.getVideoUrl())
                        .orderIndex(lesson.getOrderIndex())
                        .build())
                .toList();
    }
}
