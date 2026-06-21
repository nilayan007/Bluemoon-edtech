package com.bluemoon.bluemoonedtech.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private boolean published;
}
