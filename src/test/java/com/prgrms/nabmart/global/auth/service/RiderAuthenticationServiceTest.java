package com.prgrms.nabmart.global.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.prgrms.nabmart.domain.delivery.Rider;
import com.prgrms.nabmart.domain.delivery.repository.RiderRepository;
import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.global.auth.exception.DuplicateUsernameException;
import com.prgrms.nabmart.global.auth.exception.InvalidPasswordException;
import com.prgrms.nabmart.global.auth.exception.InvalidUsernameException;
import com.prgrms.nabmart.global.auth.jwt.TokenProvider;
import com.prgrms.nabmart.global.auth.jwt.dto.Claims;
import com.prgrms.nabmart.global.auth.service.request.RiderLoginCommand;
import com.prgrms.nabmart.global.auth.service.request.SignupRiderCommand;
import com.prgrms.nabmart.global.auth.service.response.RiderLoginResponse;
import com.prgrms.nabmart.global.auth.support.AuthFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class RiderAuthenticationServiceTest {

    @InjectMocks
    RiderAuthenticationService riderAuthenticationService;

    @Mock
    RiderRepository riderRepository;

    @Spy
    PasswordEncoder passwordEncoder;

    @Spy
    TokenProvider tokenProvider;

    public RiderAuthenticationServiceTest() {
        passwordEncoder = AuthFixture.mockPasswordEncoder();
        tokenProvider = AuthFixture.tokenProvider();
    }

    @Nested
    @DisplayName("signupRider 메서드 실행 시")
    class RiderSignupTest {

        SignupRiderCommand signupRiderCommand = AuthFixture.riderSignupCommand();

        @Test
        @DisplayName("성공")
        void success() {
            //given
            given(riderRepository.existsByUsername(any())).willReturn(false);

            //when
            riderAuthenticationService.signupRider(signupRiderCommand);

            //then
            then(riderRepository).should().save(any());
        }

        @Test
        @DisplayName("예외: 중복된 사용자 이름")
        void throwExceptionWhenUsernameDuplicate() {
            //given
            given(riderRepository.existsByUsername(any())).willReturn(true);

            //when
            //then
            assertThatThrownBy(() -> riderAuthenticationService.signupRider(signupRiderCommand))
                .isInstanceOf(DuplicateUsernameException.class);
        }
    }

    @Nested
    @DisplayName("riderLogin 메서드 실행 시")
    class RiderLoginTest {

        RiderLoginCommand riderLoginCommand = AuthFixture.riderLoginCommand();
        Rider rider = AuthFixture.rider();

        @Test
        @DisplayName("성공")
        void success() {
            //given
            given(riderRepository.findByUsername(any())).willReturn(Optional.ofNullable(rider));
            ReflectionTestUtils.setField(rider, "riderId", 1L);

            //when
            RiderLoginResponse riderLoginResponse
                = riderAuthenticationService.riderLogin(riderLoginCommand);

            //then
            Claims claims = tokenProvider.validateToken(riderLoginResponse.accessToken());
            assertThat(claims.userId()).isEqualTo(rider.getRiderId());
            assertThat(claims.authorities())
                .containsExactlyElementsOf(UserRole.ROLE_RIDER.getAuthorities());
        }

        @Test
        @DisplayName("예외: 존재하지 않는 사용자 이름")
        void throwExceptionWhenNotFoundUsername() {
            //given
            given(riderRepository.findByUsername(any())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> riderAuthenticationService.riderLogin(riderLoginCommand))
                .isInstanceOf(InvalidUsernameException.class);
        }

        @Test
        @DisplayName("예외: 비밀번호 불일치")
        void throwExceptionWhenInvalidPassword() {
            //given
            String invalidPassword = "invalidPassword123";
            RiderLoginCommand riderLoginCommand
                = new RiderLoginCommand(rider.getUsername(), invalidPassword);

            given(riderRepository.findByUsername(any())).willReturn(Optional.ofNullable(rider));

            //when
            //then
            assertThatThrownBy(() -> riderAuthenticationService.riderLogin(riderLoginCommand))
                .isInstanceOf(InvalidPasswordException.class);
        }
    }
}
