package com.bluemoon.bluemoonedtech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddLessonRequest {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 2000)
    private String videoUrl;

    @Positive
    private int orderIndex;
}
