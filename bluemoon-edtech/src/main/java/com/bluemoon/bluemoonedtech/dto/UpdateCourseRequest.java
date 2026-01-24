package com.bluemoon.bluemoonedtech.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCourseRequest {
    private String title;
    private String description;
    private String thumbnailUrl;
    private Boolean published;
}
