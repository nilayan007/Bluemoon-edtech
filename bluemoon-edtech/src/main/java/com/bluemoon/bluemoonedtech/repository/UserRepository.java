package com.bluemoon.bluemoonedtech.repository;


import com.bluemoon.bluemoonedtech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPublicId(String publicId);
    boolean existsByEmail(String email);
}

