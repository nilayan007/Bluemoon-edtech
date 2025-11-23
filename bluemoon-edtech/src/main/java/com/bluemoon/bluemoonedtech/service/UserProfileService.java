package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.dto.ProfileResponse;
import com.bluemoon.bluemoonedtech.dto.UpdateProfileRequest;
import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.entity.UserProfile;
import com.bluemoon.bluemoonedtech.exception.ResourceNotFoundException;
import com.bluemoon.bluemoonedtech.repository.UserProfileRepository;
import com.bluemoon.bluemoonedtech.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;

    /**
     * Update profile by user's publicId (you can change to use current authenticated user)
     */
    @Transactional
    public ProfileResponse updateProfile(String userPublicId, UpdateProfileRequest req) {
        User user = userRepository.findByPublicId(userPublicId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserProfile profile = profileRepository.findByUser(user)
                .orElseGet(() -> {
                    UserProfile p = UserProfile.builder().user(user).build();
                    return profileRepository.save(p);
                });

        if (req.getCollege() != null) profile.setCollege(req.getCollege());
        if (req.getYear() != null) profile.setYear(req.getYear());
        if (req.getStream() != null) profile.setStream(req.getStream());
        if (req.getAbout() != null) profile.setAbout(req.getAbout());
        if (req.getAddress() != null) profile.setAddress(req.getAddress());
        if (req.getLinkedin() != null) profile.setLinkedin(req.getLinkedin());
        if (req.getGithub() != null) profile.setGithub(req.getGithub());
        if (req.getWebsite() != null) profile.setWebsite(req.getWebsite());
        if (req.getProfileImageUrl() != null) profile.setProfileImageUrl(req.getProfileImageUrl());

        UserProfile saved = profileRepository.save(profile);

        return ProfileResponse.builder()
                .userPublicId(user.getPublicId())
                .college(saved.getCollege())
                .year(saved.getYear())
                .stream(saved.getStream())
                .about(saved.getAbout())
                .address(saved.getAddress())
                .linkedin(saved.getLinkedin())
                .github(saved.getGithub())
                .website(saved.getWebsite())
                .profileImageUrl(saved.getProfileImageUrl())
                .build();
    }

    public ProfileResponse getProfile(String userPublicId) {
        User user = userRepository.findByPublicId(userPublicId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        return ProfileResponse.builder()
                .userPublicId(user.getPublicId())
                .college(profile.getCollege())
                .year(profile.getYear())
                .stream(profile.getStream())
                .about(profile.getAbout())
                .address(profile.getAddress())
                .linkedin(profile.getLinkedin())
                .github(profile.getGithub())
                .website(profile.getWebsite())
                .profileImageUrl(profile.getProfileImageUrl())
                .build();
    }
}
