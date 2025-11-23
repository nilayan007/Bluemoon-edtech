package com.bluemoon.bluemoonedtech.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // optional external id if you want
    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private String publicId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // optional profile fields
    private String college;
    private Integer year;     // e.g., 3 (for 3rd year)
    private String stream;    // e.g., "CSE"
    @Column(length = 2000)
    private String about;
    private String address;
    private String linkedin;
    private String github;
    private String website;
    private String profileImageUrl;

    @PrePersist
    public void prePersist() {
        if (publicId == null) {
            publicId = java.util.UUID.randomUUID().toString();
        }
    }
}
