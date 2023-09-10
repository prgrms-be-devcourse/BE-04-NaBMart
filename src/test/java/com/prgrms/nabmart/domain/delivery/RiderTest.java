package com.prgrms.nabmart.domain.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.prgrms.nabmart.domain.delivery.Rider.RiderBuilder;
import com.prgrms.nabmart.domain.delivery.exception.InvalidRiderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RiderTest {

    String username = "username";
    String password = "password123";
    String address = "address";

    @Nested
    @DisplayName("username 검증 시")
    class UsernameTest {

        @ParameterizedTest
        @CsvSource({
            "member", "user123"
        })
        @DisplayName("성공")
        void success(String validUsername) {
            //given
            //when
            Rider rider = Rider.builder()
                .username(validUsername)
                .password(password)
                .address(address)
                .build();

            //then
            assertThat(rider.getUsername()).isEqualTo(validUsername);
        }

        @ParameterizedTest
        @CsvSource({
            "user1", "UPPERCase", "123", "user!@#"
        })
        @DisplayName("예외: 유효하지 않은 사용자 이름")
        void throwExceptionWhenInvalidUsername(String invalidUsername) {
            //given
            //when
            RiderBuilder riderBuilder = Rider.builder()
                .username(invalidUsername)
                .password(password)
                .address(address);

            //then
            assertThatThrownBy(riderBuilder::build).isInstanceOf(InvalidRiderException.class);
        }
    }

    @Nested
    @DisplayName("password 검증 시")
    class PasswordTest {

        @ParameterizedTest
        @CsvSource({
            "password123", "word1234"
        })
        @DisplayName("성공")
        void success(String validPassword) {
            //given
            //when
            Rider rider = Rider.builder()
                .username(username)
                .password(validPassword)
                .address(address)
                .build();

            //then
            assertThat(rider.getPassword()).isEqualTo(validPassword);
        }

        @Test
        @DisplayName("예외: 패스워드가 1000자를 초과")
        void throwExceptionWhenPasswordIsNull() {
            //given
            String invalidPassword = "a".repeat(1001);

            //when
            RiderBuilder riderBuilder = Rider.builder()
                .username(username)
                .password(invalidPassword)
                .address(address);

            //then
            assertThatThrownBy(riderBuilder::build).isInstanceOf(InvalidRiderException.class);
        }
    }

    @Nested
    @DisplayName("address 검증 시")
    class AddressTest {

        @Test
        @DisplayName("성공")
        void success() {
            //given
            String validAddress = "address";

            //when
            Rider rider = Rider.builder()
                .username(username)
                .password(password)
                .address(validAddress)
                .build();

            //then
            assertThat(rider.getAddress()).isEqualTo(validAddress);
        }

        @Test
        @DisplayName("예외: 주소 길이가 200자를 초과")
        void throwExceptionWhenAddressHasInvalidLength() {
            //given
            String invalidAddress = "a".repeat(201);

            //when
            RiderBuilder riderBuilder = Rider.builder()
                .username(username)
                .password(password)
                .address(invalidAddress);

            //then
            assertThatThrownBy(riderBuilder::build).isInstanceOf(InvalidRiderException.class);
        }
    }
}