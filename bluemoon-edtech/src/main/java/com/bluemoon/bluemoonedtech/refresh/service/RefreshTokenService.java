package com.bluemoon.bluemoonedtech.refresh.service;


import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.exception.InvalidTokenException;
import com.bluemoon.bluemoonedtech.refresh.entity.RefreshToken;
import com.bluemoon.bluemoonedtech.refresh.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken create(User user) {
        log.info("Creating refresh token");
        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();
        log.info("Refresh token created successfully");
        return refreshTokenRepository.save(token);
    }

    public RefreshToken validate(String token) {
        log.info("Validating refresh token");
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Refresh token validation failed: token not found");
                    return new InvalidTokenException("Invalid refresh token");
                });

        if (refreshToken.isRevoked()) {
            log.warn("Refresh token validation failed: token revoked");
            throw new InvalidTokenException("Refresh token revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Refresh token validation failed: token expired");
            throw new InvalidTokenException("Refresh token expired");
        }

        log.info("Refresh token validated successfully");


        return refreshToken;
    }
    public void logout(String refreshTokenValue) {

        log.info("Logout request received");

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> {
                    log.warn("Logout failed: invalid refresh token");
                    return new InvalidTokenException("Invalid refresh token");
                });

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        log.info("User logged out and refresh token revoked");
    }


    public void revoke(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }
    public void revokeAllForUser(User user) {
        log.info("Revoking all refresh tokens for user");

        refreshTokenRepository.deleteByUser(user);

        log.info("All refresh tokens revoked for user");
    }

}

