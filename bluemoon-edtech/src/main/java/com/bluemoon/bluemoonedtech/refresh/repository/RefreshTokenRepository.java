package com.bluemoon.bluemoonedtech.refresh.repository;

import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.refresh.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}

