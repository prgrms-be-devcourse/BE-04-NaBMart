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

class SignupRiderRequestTest {

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
            SignupRiderRequest signupRiderRequest
                = new SignupRiderRequest(validUsername, password, address);

            //when
            Set<ConstraintViolation<SignupRiderRequest>> result
                = validator.validate(signupRiderRequest);

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
            SignupRiderRequest signupRiderRequest
                = new SignupRiderRequest(invalidUsername, password, address);

            //when
            Set<ConstraintViolation<SignupRiderRequest>> result
                = validator.validate(signupRiderRequest);

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
            SignupRiderRequest signupRiderRequest
                = new SignupRiderRequest(nullUsername, password, address);

            //when
            Set<ConstraintViolation<SignupRiderRequest>> result
                = validator.validate(signupRiderRequest);

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
            SignupRiderRequest signupRiderRequest
                = new SignupRiderRequest(username, validPassword, address);

            //when
            Set<ConstraintViolation<SignupRiderRequest>> result
                = validator.validate(signupRiderRequest);

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
            SignupRiderRequest signupRiderRequest
                = new SignupRiderRequest(username, invalidPassword, address);

            //when
            Set<ConstraintViolation<SignupRiderRequest>> result
                = validator.validate(signupRiderRequest);

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
            SignupRiderRequest signupRiderRequest
                = new SignupRiderRequest(username, nullPassword, address);

            //when
            Set<ConstraintViolation<SignupRiderRequest>> result
                = validator.validate(signupRiderRequest);

            //then
            assertThat(result).hasSize(1);
            assertThat(result).map(ConstraintViolation::getInvalidValue)
                .containsOnlyNulls();
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
            SignupRiderRequest signupRiderRequest
                = new SignupRiderRequest(username, password, validAddress);

            //when
            Set<ConstraintViolation<SignupRiderRequest>> result
                = validator.validate(signupRiderRequest);

            //then
            assertThat(result).hasSize(0);
        }

        @Test
        @DisplayName("예외: 주소 길이가 200자를 초과")
        void throwExceptionWhenAddressHasInvalidLength() {
            //given
            String invalidAddress = "a".repeat(201);
            SignupRiderRequest signupRiderRequest
                = new SignupRiderRequest(username, password, invalidAddress);

            //when
            Set<ConstraintViolation<SignupRiderRequest>> result
                = validator.validate(signupRiderRequest);

            //then
            assertThat(result).hasSize(1);
            assertThat(result).map(ConstraintViolation::getInvalidValue)
                .containsOnly(invalidAddress);
        }

        @Test
        @DisplayName("예외: 주소가 공백")
        void throwExceptionWhenAddressIsBlank() {
            //given
            String invalidAddress = "  ";
            SignupRiderRequest signupRiderRequest
                = new SignupRiderRequest(username, password, invalidAddress);

            //when
            Set<ConstraintViolation<SignupRiderRequest>> result
                = validator.validate(signupRiderRequest);

            //then
            assertThat(result).hasSize(1);
            assertThat(result).map(ConstraintViolation::getInvalidValue)
                .containsOnly(invalidAddress);
        }

        @Test
        @DisplayName("예외: 주소가 null")
        void throwExceptionWhenAddressIsNull() {
            //given
            String nullAddress = null;
            SignupRiderRequest signupRiderRequest
                = new SignupRiderRequest(username, password, nullAddress);

            //when
            Set<ConstraintViolation<SignupRiderRequest>> result
                = validator.validate(signupRiderRequest);

            //then
            assertThat(result).hasSize(1);
            assertThat(result).map(ConstraintViolation::getInvalidValue)
                .containsOnlyNulls();
        }
    }
}