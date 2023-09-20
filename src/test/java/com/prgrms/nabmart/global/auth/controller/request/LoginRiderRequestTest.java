package com.prgrms.nabmart.global.auth.controller.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LoginRiderRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    String username = "username";
    String password = "password123";
    String address = "address";

    @Nested
    @DisplayName("username 검증 시")
    class UsernameTes {

        @ParameterizedTest
        @CsvSource({
            "member", "user123"
        })
        @DisplayName("성공")
        void success(String validUsername) {
            //given
            LoginRiderRequest loginRiderRequest = new LoginRiderRequest(validUsername, password);

            //when
            Set<ConstraintViolation<LoginRiderRequest>> result
                = validator.validate(loginRiderRequest);

            //then
            assertThat(result).hasSize(0);
        }

        @ParameterizedTest
        @CsvSource({
            "user1", "UPPERCase", "123", "user!@#"
        })
        @DisplayName("예외: 유효하지 않은 사용자 이름")
        void throwExceptionWhenInvalidUsername(String invalidUsername) {
            //given
            LoginRiderRequest loginRiderRequest = new LoginRiderRequest(invalidUsername, password);

            //when
            Set<ConstraintViolation<LoginRiderRequest>> result
                = validator.validate(loginRiderRequest);

            //then
            assertThat(result).hasSize(1);
            assertThat(result).map(ConstraintViolation::getInvalidValue)
                .containsOnly(invalidUsername);
        }

        @Test
        @DisplayName("예외: 사용자 이름이 null")
        void throwExceptionWhenUsernameIsNull() {
            //given
            String nullUsername = null;
            LoginRiderRequest loginRiderRequest = new LoginRiderRequest(nullUsername, password);

            //when
            Set<ConstraintViolation<LoginRiderRequest>> result
                = validator.validate(loginRiderRequest);
            //then
            assertThat(result).hasSize(1);
            assertThat(result).map(ConstraintViolation::getInvalidValue)
                .containsOnlyNulls();
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
            LoginRiderRequest loginRiderRequest = new LoginRiderRequest(username, validPassword);

            //when
            Set<ConstraintViolation<LoginRiderRequest>> result
                = validator.validate(loginRiderRequest);

            //then
            assertThat(result).hasSize(0);
        }

        @ParameterizedTest
        @CsvSource({
            "password", "UPPERCase123", "1234", "password!@#$%"
        })
        @DisplayName("예외: 유효하지 않은 패스워드")
        void throwExceptionWhenInvalidPassword(String invalidPassword) {
            //given
            LoginRiderRequest loginRiderRequest = new LoginRiderRequest(username, invalidPassword);

            //when
            Set<ConstraintViolation<LoginRiderRequest>> result
                = validator.validate(loginRiderRequest);

            //then
            assertThat(result).hasSize(1);
            assertThat(result).map(ConstraintViolation::getInvalidValue)
                .containsOnly(invalidPassword);
        }

        @Test
        @DisplayName("예외: 패스워드가 null")
        void throwExceptionWhenPasswordIsNull() {
            //given
            String nullPassword = null;
            LoginRiderRequest loginRiderRequest = new LoginRiderRequest(username, nullPassword);

            //when
            Set<ConstraintViolation<LoginRiderRequest>> result
                = validator.validate(loginRiderRequest);

            //then
            assertThat(result).hasSize(1);
            assertThat(result).map(ConstraintViolation::getInvalidValue)
                .containsOnlyNulls();
        }
    }
}
