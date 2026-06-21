package com.bluemoon.bluemoonedtech.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLessonRequest {
    @Size(min = 1, max = 255)
    private String title;

    @Size(min = 1, max = 2000)
    private String videoUrl;

    @Positive
    private Integer orderIndex;
}
