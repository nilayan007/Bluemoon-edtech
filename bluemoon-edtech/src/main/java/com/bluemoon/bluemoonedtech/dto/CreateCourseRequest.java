package com.bluemoon.bluemoonedtech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCourseRequest {
    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 2000)
    private String description;

    @Size(max = 2000)
    private String thumbnailUrl;
}
