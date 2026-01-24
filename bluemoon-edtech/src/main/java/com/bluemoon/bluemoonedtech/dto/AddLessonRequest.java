package com.bluemoon.bluemoonedtech.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddLessonRequest {
    private String title;
    private String videoUrl;
    private int orderIndex;
}
