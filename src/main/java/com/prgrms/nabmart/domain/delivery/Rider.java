package com.prgrms.nabmart.domain.delivery;

import static java.util.Objects.nonNull;

import com.prgrms.nabmart.domain.delivery.exception.InvalidRiderException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rider {

    private static final Pattern USERNAME_PATTERN
        = Pattern.compile("^(?=.*[a-z])[a-z0-9]{6,20}$");
    private static final int PASSWORD_LENGTH = 1000;
    private static final int ADDRESS_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long riderId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Builder
    public Rider(final String username, final String password, final String address) {
        validateUsername(username);
        validatePassword(password);
        validateAddress(address);
        this.username = username;
        this.password = password;
        this.address = address;
    }

    private void validateUsername(String username) {
        if (nonNull(username) && !USERNAME_PATTERN.matcher(username).matches()) {
            throw new InvalidRiderException(
                "사용자 이름은 영어 소문자 또는 영어 소문자와 숫자 6자 이상, 20자 이하로 구성 되어야 합니다.");
        }
    }

    private void validatePassword(String password) {
        if (nonNull(password) && password.length() > PASSWORD_LENGTH) {
            throw new InvalidRiderException("유효하지 않은 패스워드 입니다.");
        }
    }

    private void validateAddress(String address) {
        if (nonNull(address) && address.length() > ADDRESS_LENGTH) {
            throw new InvalidRiderException("주소의 길이는 최대 200자 입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rider rider = (Rider) o;
        return Objects.equals(username, rider.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
