package com.bluemoon.bluemoonedtech.dto;
import lombok.Getter;
import lombok.Builder;
@Builder
@Getter
public class LoginResponse {
    private String id;       // publicId
    private String name;
    private String email;
    private boolean verified;
    private String token;
}
