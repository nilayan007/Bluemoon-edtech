package com.bluemoon.bluemoonedtech.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponse {
    private String userPublicId;   // user's publicId (UUID)
    private String college;
    private Integer year;
    private String stream;
    private String about;
    private String address;
    private String linkedin;
    private String github;
    private String website;
    private String profileImageUrl;
}
