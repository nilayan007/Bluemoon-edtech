package com.bluemoon.bluemoonedtech.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {
    private String college;
    private Integer year;
    private String stream;

    @Size(max = 2000, message = "About should be <= 2000 chars")
    private String about;
    private String address;
    private String linkedin;
    private String github;
    private String website;
    private String profileImageUrl;
}
