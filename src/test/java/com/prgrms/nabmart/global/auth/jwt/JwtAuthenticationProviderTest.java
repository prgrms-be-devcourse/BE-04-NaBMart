package com.prgrms.nabmart.global.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.service.response.RegisterUserResponse;
import com.prgrms.nabmart.global.auth.jwt.dto.JwtAuthentication;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class JwtAuthenticationProviderTest {

    JwtAuthenticationProvider jwtAuthenticationProvider;
    TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new JavaJwtTokenProvider(
                "issuer",
                "thisisnotrandomclientsecretjustforatest",
                60
        );
        jwtAuthenticationProvider = new JwtAuthenticationProvider(tokenProvider);
    }

    @Nested
    @DisplayName("authenticate 메서드 실행 시")
    class AuthenticateTest {

        RegisterUserResponse userResponse;
        String accessToken;

        @BeforeEach
        void setUp() {
            userResponse = new RegisterUserResponse(1L, UserRole.ROLE_USER);
            accessToken = tokenProvider.createToken(userResponse);
        }

        @Test
        @DisplayName("성공: principal은 JwtAuthentication 인스턴스")
        void successWhenAuthenticationPrincipalIsInstanceOfJwtAuthentication() {
            //given
            //when
            Authentication authentication = jwtAuthenticationProvider.authenticate(accessToken);

            //then
            Object principal = authentication.getPrincipal();
            assertThat(principal).isInstanceOf(JwtAuthentication.class);
            JwtAuthentication jwtAuthentication = (JwtAuthentication) principal;
            assertThat(jwtAuthentication.userId()).isEqualTo(userResponse.userId());
            assertThat(jwtAuthentication.accessToken()).isEqualTo(accessToken);
        }

        @Test
        @DisplayName("성공: authority는 토큰의 claim과 동일")
        void successWhenAuthorityEqualsAccessTokenClaimsRole() {
            //given
            //when
            Authentication authentication = jwtAuthenticationProvider.authenticate(accessToken);

            //then
            List<GrantedAuthority> findAuthorities = (List<GrantedAuthority>) authentication.getAuthorities();
            List<GrantedAuthority> givenAuthorities = userResponse.userRole().getAuthorities().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            assertThat(findAuthorities).containsAnyElementsOf(givenAuthorities);
        }
    }
}