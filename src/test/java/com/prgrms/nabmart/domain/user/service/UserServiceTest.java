package com.prgrms.nabmart.domain.user.service;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.service.request.RegisterUserCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    private User createUser() {
        return new User(
                "넌나에게모욕감을줬어",
                "nayb",
                "naybProviderId",
                UserRole.ROLE_USER
        );
    }

    @Nested
    @DisplayName("getOrRegisterUser 메서드 실행 시")
    class GetOrRegisterUserTest {

        RegisterUserCommand registerUserCommand;

        @BeforeEach
        void setUp() {
            registerUserCommand = RegisterUserCommand.builder()
                    .nickname("닉네임")
                    .provider("kakao")
                    .providerId("kakaoProviderId")
                    .userRole(UserRole.ROLE_USER)
                    .build();
        }

        @Test
        @DisplayName("성공: User가 존재하면 User 반환")
        void getUserWhenUserExists() {
            //given
            User user = createUser();
            given(userRepository.findByProviderAndProviderId(any(), any())).willReturn(Optional.ofNullable(user));

            //when
            userService.getOrRegisterUser(registerUserCommand);

            //then
            then(userRepository).should(times(0)).save(any());
        }

        @Test
        @DisplayName("성공: User가 존재하지 않으면 User 생성 후 저장")
        void getUserWhenUserNotFound() {
            //given
            given(userRepository.findByProviderAndProviderId(any(), any())).willReturn(Optional.empty());

            //when
            userService.getOrRegisterUser(registerUserCommand);

            //then
            then(userRepository).should(times(1)).save(any());
        }
    }
}