package com.bluemoon.bluemoonedtech.refresh.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequest {

    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
