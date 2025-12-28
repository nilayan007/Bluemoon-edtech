package com.bluemoon.bluemoonedtech.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmailChangeOtpRequest {

    @NotBlank
    @Email
    private String newEmail;

    @NotBlank
    private String otp;
}

