package com.prgrms.nabmart.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.service.request.FindUserCommand;
import com.prgrms.nabmart.domain.user.service.request.RegisterUserCommand;
import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Nested
    @DisplayName("getOrRegisterUser 메서드 실행 시")
    class GetOrRegisterUserTest {

        RegisterUserCommand registerUserCommand = UserFixture.registerUserCommand();

        @Test
        @DisplayName("성공: User가 존재하면 User 반환")
        void getUserWhenUserExists() {
            //given
            User user = UserFixture.user();
            given(userRepository.findByProviderAndProviderId(any(), any())).willReturn(
                Optional.ofNullable(user));

            //when
            userService.getOrRegisterUser(registerUserCommand);

            //then
            then(userRepository).should(times(0)).save(any());
        }

        @Test
        @DisplayName("성공: User가 존재하지 않으면 User 생성 후 저장")
        void getUserWhenUserNotFound() {
            //given
            given(userRepository.findByProviderAndProviderId(any(), any())).willReturn(
                Optional.empty());

            //when
            userService.getOrRegisterUser(registerUserCommand);

            //then
            then(userRepository).should(times(1)).save(any());
        }
    }

    @Nested
    @DisplayName("findUser 메서드 실행 시")
    class FindUserTest {

        FindUserCommand findUserCommand = UserFixture.findUserCommand();

        @Test
        @DisplayName("성공")
        void success() {
            //given
            User user = UserFixture.user();

            given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));

            //when
            FindUserDetailResponse result = userService.findUser(findUserCommand);

            //then
            assertThat(result.nickname()).isEqualTo(user.getNickname());
            assertThat(result.email()).isEqualTo(user.getEmail());
            assertThat(result.provider()).isEqualTo(user.getProvider());
            assertThat(result.providerId()).isEqualTo(user.getProviderId());
            assertThat(result.userRole()).isEqualTo(user.getUserRole());
            assertThat(result.userGrade()).isEqualTo(user.getUserGrade());
        }

        @Test
        @DisplayName("예외: 존재하지 않는 User")
        void throwExceptionWhenUserNotFound() {
            //given
            given(userRepository.findById(any())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> userService.findUser(findUserCommand))
                .isInstanceOf(NotFoundUserException.class);
        }
    }
}
