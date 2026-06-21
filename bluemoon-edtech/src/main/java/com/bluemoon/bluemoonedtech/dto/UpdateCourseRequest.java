package com.bluemoon.bluemoonedtech.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCourseRequest {
    @Size(min = 1, max = 255)
    private String title;

    @Size(max = 2000)
    private String description;

    @Size(max = 2000)
    private String thumbnailUrl;
    private Boolean published;
}
