package com.bluemoon.bluemoonedtech.service;


import com.bluemoon.bluemoonedtech.dto.RegisterRequest;
import com.bluemoon.bluemoonedtech.dto.UserResponse;
import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.entity.UserProfile;
import com.bluemoon.bluemoonedtech.entity.UserRole;
import com.bluemoon.bluemoonedtech.repository.UserProfileRepository;
import com.bluemoon.bluemoonedtech.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String email = request.getEmail().toLowerCase().trim();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(email)
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.STUDENT)
                .build();

        User saved = userRepository.save(user);

        // create empty profile
        UserProfile profile = UserProfile.builder()
                .user(saved)
                .build();
        profileRepository.save(profile);
        saved.setProfile(profile);

        return UserResponse.builder()
                .id(saved.getPublicId())
                .name(saved.getName())
                .email(saved.getEmail())
                .role(saved.getRole())
                .build();
    }
}
