package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.dto.LoginRequest;
import com.bluemoon.bluemoonedtech.dto.LoginResponse;
import com.bluemoon.bluemoonedtech.dto.AccessTokenResponse;
import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.refresh.entity.RefreshToken;
import com.bluemoon.bluemoonedtech.refresh.service.RefreshTokenService;
import com.bluemoon.bluemoonedtech.security.CustomUserDetails;
import com.bluemoon.bluemoonedtech.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    public LoginResponse login(LoginRequest request) {

        String email = request.getEmail().toLowerCase().trim();

        log.info("Login attempt initiated");

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        CustomUserDetails userDetails =
                (CustomUserDetails) auth.getPrincipal();

        User user = userDetails.getUser();

        log.info("User authenticated successfully");

        String accessToken = jwtUtils.generateToken(user.getPublicId());
        RefreshToken refreshToken = refreshTokenService.create(user);

        log.info("Access and refresh tokens generated successfully");

        return LoginResponse.builder()
                .id(user.getPublicId())
                .name(user.getName())
                .email(user.getEmail())
                .verified(Boolean.TRUE.equals(user.getIsVerified()))
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AccessTokenResponse refresh(String refreshTokenValue) {

        log.info("Refresh token request received");

        RefreshToken refreshToken =
                refreshTokenService.validate(refreshTokenValue);

        User user = refreshToken.getUser();

        String newAccessToken =
                jwtUtils.generateToken(user.getPublicId());

        log.info("New access token generated successfully");

        return new AccessTokenResponse(newAccessToken);
    }

    public void logout(String refreshToken) {

        log.info("Logout request initiated");

        refreshTokenService.logout(refreshToken);

        log.info("User logged out successfully");
    }
}
