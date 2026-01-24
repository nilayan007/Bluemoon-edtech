package com.bluemoon.bluemoonedtech.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLessonRequest {
    private String title;
    private String videoUrl;
    private Integer orderIndex;
}
