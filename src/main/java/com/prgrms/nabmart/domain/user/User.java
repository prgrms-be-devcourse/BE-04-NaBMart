package com.prgrms.nabmart.domain.user;

import com.prgrms.nabmart.domain.BaseTimeEntity;
import com.prgrms.nabmart.domain.user.exception.InvalidEmailException;
import com.prgrms.nabmart.domain.user.exception.InvalidNicknameException;
import jakarta.persistence.*;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

    private static final int NICKNAME_LENGTH = 200;
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]{1,64}@([a-zA-Z0-9.-]{1,255})$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String email;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserGrade userGrade;

    @Builder
    public User(
        final String nickname,
        final String email,
        final String provider,
        final String providerId,
        final UserRole userRole,
        final UserGrade userGrade) {
        validateNickname(nickname);
        validateEmail(email);
        this.nickname = nickname;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.userRole = userRole;
        this.userGrade = userGrade;
    }

    private void validateNickname(String nickname) {
        if (nickname.length() > NICKNAME_LENGTH) {
            throw new InvalidNicknameException("사용할 수 없는 닉네임입니다.");
        }
    }

    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailException("사용할 수 없는 이메일입니다.");
        }
    }
}
