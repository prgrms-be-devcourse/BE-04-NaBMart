package com.prgrms.nabmart.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String nickname;

    private String provider;
    private String providerId;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String nickname, String provider, String providerId, UserRole userRole) {
        this.nickname = nickname;
        this.provider = provider;
        this.providerId = providerId;
        this.userRole = userRole;
    }
}

