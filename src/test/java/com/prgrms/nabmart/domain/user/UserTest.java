package com.prgrms.nabmart.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.prgrms.nabmart.domain.user.User.UserBuilder;
import com.prgrms.nabmart.domain.user.exception.InvalidEmailException;
import com.prgrms.nabmart.domain.user.exception.InvalidNicknameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class UserTest {

    private static final String NICKNAME = "nickname";
    private static final String EMAIL = "email@example.com";
    private static final String PROVIDER = "nayb";
    private static final String PROVIDER_ID = "123123";
    private static final UserRole USER_ROLE = UserRole.ROLE_USER;

    @Nested
    @DisplayName("User 생성 시")
    class NewUserTest {

        @ParameterizedTest
        @CsvSource({
            "가나다", "abc", "123", "가나다123", "abc123", "가나다abc123", "가나다abc123!@#"
        })
        @DisplayName("성공: 유효한 User 닉네임")
        void successWhenValidNickname(String validNickname) {
            //given
            //when
            User newUser = User.builder()
                .nickname(validNickname)
                .email(EMAIL)
                .provider(PROVIDER)
                .providerId(PROVIDER_ID)
                .userRole(USER_ROLE)
                .build();

            //then
            assertThat(newUser.getNickname()).isEqualTo(validNickname);
        }

        @Test
        @DisplayName("예외: 200자를 초과한 닉네임")
        void throwExceptionWhenInvalidNickname() {
            //given
            String invalidNickname = "a".repeat(201);

            //when
            //then
            UserBuilder userBuilder = User.builder()
                .nickname(invalidNickname)
                .email(EMAIL)
                .provider(PROVIDER)
                .providerId("123")
                .userRole(UserRole.ROLE_USER);
            assertThatThrownBy(userBuilder::build).isInstanceOf(InvalidNicknameException.class);
        }

        @ParameterizedTest
        @CsvSource({
            "abc@example.com", "abcABC123@example.org"
        })
        @DisplayName("성공: 유효한 User 이메일")
        void successWhenValidEmail(String validEmail) {
            //given
            //when
            User newUser = User.builder()
                .nickname(validEmail)
                .email(EMAIL)
                .provider(PROVIDER)
                .providerId(PROVIDER_ID)
                .userRole(USER_ROLE)
                .build();

            //then
            assertThat(newUser.getNickname()).isEqualTo(validEmail);
        }

        @ParameterizedTest
        @CsvSource({
            "abc123.com", "abc@abc", "abc", "abc@.com", "abc@abc."
        })
        @DisplayName("예외: 잘못된 형식의 이메일")
        void throwExceptionWhenInvalidEmail() {
            //given
            String invalidEmail = "abd123.com";

            //when
            //then
            UserBuilder userBuilder = User.builder()
                .nickname(NICKNAME)
                .email(invalidEmail)
                .provider(PROVIDER)
                .providerId(PROVIDER_ID)
                .userRole(USER_ROLE);
            assertThatThrownBy(userBuilder::build).isInstanceOf(InvalidEmailException.class);
        }

        @Test
        @DisplayName("예외: 사용자 이름이 64자를 초과하는 이메일")
        void throwExceptionWhenInvalidEmailName() {
            //given
            String invalidEmailName = "a".repeat(65) + "@example.com";

            //when
            //then
            UserBuilder userBuilder = User.builder()
                .nickname(NICKNAME)
                .email(invalidEmailName)
                .provider(PROVIDER)
                .providerId(PROVIDER_ID)
                .userRole(USER_ROLE);
            assertThatThrownBy(userBuilder::build).isInstanceOf(InvalidEmailException.class);
        }

        @Test
        @DisplayName("예외: 도메인이 255자를 초과하는 이메일")
        void throwExceptionWhenInvalidDomain() {
            //given
            String invalidEmailDomain = "abc@" + "a".repeat(252) + ".com";

            //when
            //then
            UserBuilder userBuilder = User.builder()
                .nickname(NICKNAME)
                .email(invalidEmailDomain)
                .provider(PROVIDER)
                .providerId(PROVIDER_ID)
                .userRole(USER_ROLE);
            assertThatThrownBy(userBuilder::build).isInstanceOf(InvalidEmailException.class);
        }
    }
}
