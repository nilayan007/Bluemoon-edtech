package com.bluemoon.bluemoonedtech.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LessonResponseDTO {
    private Long id;
    private String title;
    private String videoUrl;
    private int orderIndex;
}
