package com.bluemoon.bluemoonedtech.service;


import com.bluemoon.bluemoonedtech.dto.LoginRequest;
import com.bluemoon.bluemoonedtech.dto.LoginResponse;
import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.repository.UserRepository;
import com.bluemoon.bluemoonedtech.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;


    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail().toLowerCase().trim();


        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );


// after authentication success, fetch user and generate token
        User user = userRepository.findByEmail(email).orElseThrow();
        String token = jwtUtils.generateToken(user.getPublicId()); // we use publicId as subject


        return LoginResponse.builder()
                .id(user.getPublicId())
                .name(user.getName())
                .email(user.getEmail())
                .verified(Boolean.TRUE.equals(user.getIsVerified())).token(token)
                .build();
// Note: if you want to return token in response, add a field to LoginResponse and set token.
    }
}