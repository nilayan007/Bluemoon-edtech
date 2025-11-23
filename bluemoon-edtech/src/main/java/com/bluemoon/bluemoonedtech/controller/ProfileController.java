package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.dto.ProfileResponse;
import com.bluemoon.bluemoonedtech.dto.UpdateProfileRequest;
import com.bluemoon.bluemoonedtech.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserProfileService userProfileService;

    /**
     * Temporary: update by userPublicId in a path.
     * In production,use the authenticated user's id.
     */
    @PutMapping("/{userPublicId}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable String userPublicId,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        ProfileResponse response = userProfileService.updateProfile(userPublicId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userPublicId}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable String userPublicId) {
        ProfileResponse response = userProfileService.getProfile(userPublicId);
        return ResponseEntity.ok(response);
    }
}
