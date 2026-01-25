package com.bluemoon.bluemoonedtech.service;

import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.entity.UserRole;
import com.bluemoon.bluemoonedtech.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBootstrapService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.name}")
    private String adminName;

    @Value("${admin.password}")
    private String adminPassword;

    @PostConstruct
    public void createAdminIfNotExists() {

        // Safety check
        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }



        User admin = User.builder()
                .email(adminEmail)
                .name(adminName)
                .passwordHash(passwordEncoder.encode(adminPassword))
                .role(UserRole.ADMIN).isVerified(true)
                .build();


        userRepository.save(admin);

        System.out.println(" Default admin user created: " + adminEmail);
    }
}
