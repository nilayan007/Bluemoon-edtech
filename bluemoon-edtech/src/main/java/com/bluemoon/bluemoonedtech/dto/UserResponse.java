package com.bluemoon.bluemoonedtech.dto;

import com.bluemoon.bluemoonedtech.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private String id;   // publicId (UUID)
    private String name;
    private String email;
    private UserRole role;
}

