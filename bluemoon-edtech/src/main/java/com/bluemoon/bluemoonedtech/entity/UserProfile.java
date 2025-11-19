package com.bluemoon.bluemoonedtech.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String college;
    private String university;
    private String year;       // "1st Year", "2026 Passout"
    private String stream;     // CSE, ECE, etc.

    @Column(length = 1000)
    private String about;

    private String city;
    private String country;
    private String gender;

    private LocalDate dateOfBirth;

    private String profileImageUrl;
}

