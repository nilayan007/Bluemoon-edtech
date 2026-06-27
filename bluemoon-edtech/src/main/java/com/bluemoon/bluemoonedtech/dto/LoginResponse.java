package com.bluemoon.bluemoonedtech.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Builder;
@Builder
@Getter
public class LoginResponse {
    private String id;       // publicId
    private String name;
    private String email;
    private boolean verified;
    private String accessToken;
    @JsonIgnore
    private String refreshToken;
}
