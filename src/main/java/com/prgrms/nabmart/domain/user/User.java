package com.prgrms.nabmart.domain.user;

import com.prgrms.nabmart.domain.user.exception.InvalidNicknameException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    private static final int NICKNAME_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    public User(String nickname, String provider, String providerId, UserRole userRole) {
        validateNickname(nickname);
        this.nickname = nickname;
        this.provider = provider;
        this.providerId = providerId;
        this.userRole = userRole;
    }

    private void validateNickname(String nickname) {
        if (nickname.length() > NICKNAME_LENGTH) {
            throw new InvalidNicknameException("사용할 수 없는 닉네임입니다.");
        }
    }
}
