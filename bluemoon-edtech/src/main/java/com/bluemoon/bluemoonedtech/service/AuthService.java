package com.bluemoon.bluemoonedtech.service;


import com.bluemoon.bluemoonedtech.dto.LoginRequest;
import com.bluemoon.bluemoonedtech.dto.LoginResponse;
import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.refresh.entity.RefreshToken;
import com.bluemoon.bluemoonedtech.refresh.service.RefreshTokenService;
import com.bluemoon.bluemoonedtech.repository.UserRepository;
import com.bluemoon.bluemoonedtech.security.CustomUserDetails;
import com.bluemoon.bluemoonedtech.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import com.bluemoon.bluemoonedtech.dto.AccessTokenResponse;
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
    //private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;



    public LoginResponse login(LoginRequest request) {

        String email = request.getEmail().toLowerCase().trim();


        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );
        CustomUserDetails userDetails =
                (CustomUserDetails) auth.getPrincipal();

        User user = userDetails.getUser();


// after authentication success, fetch user and generate token
        String token = jwtUtils.generateToken(user.getPublicId()); //
        RefreshToken refreshToken =
                refreshTokenService.create(user);

        return LoginResponse.builder()
                .id(user.getPublicId())
                .name(user.getName())
                .email(user.getEmail())
                .verified(Boolean.TRUE.equals(user.getIsVerified())).accessToken(token)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AccessTokenResponse refresh(String refreshTokenValue) {

        RefreshToken refreshToken =
                refreshTokenService.validate(refreshTokenValue);

        User user = refreshToken.getUser();

        String newAccessToken =
                jwtUtils.generateToken(user.getPublicId());

        return new AccessTokenResponse(newAccessToken);
    }

    public void logout(String refreshToken) {
        refreshTokenService.logout(refreshToken);
    }


}