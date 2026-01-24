package com.bluemoon.bluemoonedtech.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCourseRequest {
    private String title;
    private String description;
    private String thumbnailUrl;
}
