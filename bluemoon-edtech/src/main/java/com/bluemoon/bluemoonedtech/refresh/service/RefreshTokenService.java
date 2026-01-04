package com.bluemoon.bluemoonedtech.refresh.service;


import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.exception.InvalidTokenException;
import com.bluemoon.bluemoonedtech.refresh.entity.RefreshToken;
import com.bluemoon.bluemoonedtech.refresh.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshToken create(User user) {
        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();

        return repository.save(token);
    }

    public RefreshToken validate(String token) {
        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        if (refreshToken.isRevoked())
            throw new InvalidTokenException("Refresh token revoked");

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new InvalidTokenException("Refresh token expired");

        return refreshToken;
    }
    public void logout(String refreshTokenValue) {

        RefreshToken refreshToken = repository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        refreshToken.setRevoked(true); // OR delete
        repository.save(refreshToken);
    }


    public void revoke(RefreshToken token) {
        token.setRevoked(true);
        repository.save(token);
    }
}

