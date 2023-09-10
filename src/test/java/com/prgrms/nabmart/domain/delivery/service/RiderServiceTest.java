package com.prgrms.nabmart.domain.delivery.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.prgrms.nabmart.domain.delivery.exception.DuplicateRiderUsernameException;
import com.prgrms.nabmart.domain.delivery.repository.RiderRepository;
import com.prgrms.nabmart.domain.delivery.service.request.RiderSignupCommand;
import com.prgrms.nabmart.domain.delivery.support.RiderFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class RiderServiceTest {

    @InjectMocks
    RiderService riderService;

    @Mock
    RiderRepository riderRepository;

    @Spy
    PasswordEncoder passwordEncoder;

    public RiderServiceTest() {
        passwordEncoder = mockPasswordEncoder;
    }

    PasswordEncoder mockPasswordEncoder = new PasswordEncoder() {
        @Override
        public String encode(CharSequence rawPassword) {
            return new StringBuilder(rawPassword).reverse().toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return encode(rawPassword).equals(encodedPassword);
        }
    };

    @Nested
    @DisplayName("riderSignup 메서드 실행 시")
    class RiderSignupTest {

        RiderSignupCommand riderSignupCommand = RiderFixture.riderSignupCommand();

        @Test
        @DisplayName("성공")
        void success() {
            //given
            given(riderRepository.existsByUsername(any())).willReturn(false);

            //when
            riderService.riderSignup(riderSignupCommand);

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
            assertThatThrownBy(() -> riderService.riderSignup(riderSignupCommand))
                .isInstanceOf(DuplicateRiderUsernameException.class);
        }
    }
}
