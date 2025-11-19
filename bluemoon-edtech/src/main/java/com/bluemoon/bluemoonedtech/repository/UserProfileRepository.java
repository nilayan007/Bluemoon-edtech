package com.bluemoon.bluemoonedtech.repository;


import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(User user);
}
