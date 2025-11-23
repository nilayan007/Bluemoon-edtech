package com.bluemoon.bluemoonedtech.service;


import com.bluemoon.bluemoonedtech.dto.LoginRequest;
import com.bluemoon.bluemoonedtech.dto.LoginResponse;
import com.bluemoon.bluemoonedtech.dto.RegisterRequest;
import com.bluemoon.bluemoonedtech.dto.UserResponse;
import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.entity.UserProfile;
import com.bluemoon.bluemoonedtech.entity.UserRole;
import com.bluemoon.bluemoonedtech.repository.UserProfileRepository;
import com.bluemoon.bluemoonedtech.repository.UserRepository;
import com.bluemoon.bluemoonedtech.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
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
                .role(UserRole.STUDENT).isVerified(true)
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
    /*public LoginResponse login(LoginRequest request) {

        String email = request.getEmail().toLowerCase().trim();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        Map<String, Object> claims = Map.of(
                "role", user.getRole().name(),
                "name", user.getName()
        );

        // subject — better to use publicId so you don't expose internal long id
        String token = jwtService.generateToken(user.getPublicId(), claims);

        return LoginResponse.builder()
                .id(user.getPublicId())
                .name(user.getName())
                .email(user.getEmail())
                .verified(user.getIsVerified()).token(token)
                .build();
    }*/


}
