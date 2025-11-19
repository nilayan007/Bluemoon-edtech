package com.bluemoon.bluemoonedtech.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                       // internal PK

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private String publicId;               // external UUID

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // OneToOne mapped by profile.user if you add later:
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserProfile profile;

    @PrePersist
    public void prePersist() {
        if (publicId == null) publicId = UUID.randomUUID().toString();
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (isVerified == null) isVerified = false;
        if (role == null) role = UserRole.STUDENT;
    }
}
