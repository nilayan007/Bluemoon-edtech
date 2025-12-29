package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.dto.ProfileResponse;
import com.bluemoon.bluemoonedtech.dto.UpdateProfileRequest;
import com.bluemoon.bluemoonedtech.security.CustomUserDetails;
import com.bluemoon.bluemoonedtech.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserProfileService userProfileService;


    @PutMapping
    public ResponseEntity<String> updateProfile(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        ProfileResponse response =
                userProfileService.updateProfile(user.getId(), request);

        return ResponseEntity.ok("User profile details updated Successfully");
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        ProfileResponse response =
                userProfileService.getProfile(user.getId());

        return ResponseEntity.ok(response);
    }

}
