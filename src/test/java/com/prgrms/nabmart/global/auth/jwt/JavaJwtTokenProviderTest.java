package com.prgrms.nabmart.global.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.service.response.RegisterUserResponse;
import com.prgrms.nabmart.global.auth.exception.InvalidJwtException;
import com.prgrms.nabmart.global.auth.jwt.dto.Claims;
import com.prgrms.nabmart.global.fixture.AuthFixture;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JavaJwtTokenProviderTest {

    String clientSecret = "thisIsClientSecretForTestSoDontWorry";
    TokenProvider tokenProvider = new JavaJwtTokenProvider(
        "issuer",
        clientSecret,
        60
    );

    @Nested
    @DisplayName("createToken 메서드 실행 시")
    class CreateTokenTest {

        @Test
        @DisplayName("성공: 토큰 반환")
        void success() {
            //given
            RegisterUserResponse registerUserResponse = AuthFixture.registerUserResponse();

            //when
            String token = tokenProvider.createToken(registerUserResponse);

            //then
            Claims claims = tokenProvider.validateToken(token);
            assertThat(claims.userId()).isEqualTo(1L);
            assertThat(claims.authorities()).containsAnyElementsOf(
                UserRole.ROLE_USER.getAuthorities());
        }
    }

    @Nested
    @DisplayName("validateToken 메서드 실행 시")
    class ValidateTokenTest {

        RegisterUserResponse registerUserResponse = AuthFixture.registerUserResponse();

        private String createToken(
            String issuer,
            int expirySeconds,
            Algorithm algorithm,
            RegisterUserResponse userResponse) {
            Date now = new Date();
            Date expiresAt = new Date(now.getTime() + expirySeconds * 1000L);
            return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withClaim("userId", userResponse.userId())
                .withClaim("role", userResponse.userRole().getValue())
                .sign(algorithm);
        }

        @Test
        @DisplayName("성공: User 정보를 담은 Claims 반환")
        void success() {
            //given
            String token = tokenProvider.createToken(registerUserResponse);

            //when
            Claims claims = tokenProvider.validateToken(token);

            //then
            assertThat(claims.userId()).isEqualTo(1L);
            assertThat(claims.authorities()).containsAnyElementsOf(
                UserRole.ROLE_USER.getAuthorities());
        }

        @Test
        @DisplayName("예외: 토큰의 알고리즘이 잘못된 경우")
        void throwExceptionWhenInvalidAlgorithm() {
            //given
            int zeroExpirySeconds = 0;
            String invalidAlgorithmToken = createToken(
                "issuer",
                zeroExpirySeconds,
                Algorithm.none(),
                registerUserResponse
            );

            //when
            //then
            assertThatThrownBy(() -> tokenProvider.validateToken(invalidAlgorithmToken))
                .isInstanceOf(InvalidJwtException.class);
        }

        @Test
        @DisplayName("예외: 토큰의 서명이 유효하지 않은 경우")
        void throwExceptionWhenInvalidIssuer() {
            //given
            String invalidIssuer = "invalidIssuer";
            String invalidIssuerToken = createToken(
                invalidIssuer,
                60,
                Algorithm.HMAC512(clientSecret),
                registerUserResponse
            );

            //when
            //then
            assertThatThrownBy(() -> tokenProvider.validateToken(invalidIssuerToken))
                .isInstanceOf(InvalidJwtException.class);
        }

        @Test
        @DisplayName("예외: 토큰이 만료된 경우")
        void throwExceptionWhenTokenExpired() {
            //given
            int zeroExpirySeconds = 0;
            String expiredToken = createToken(
                "issuer",
                zeroExpirySeconds,
                Algorithm.HMAC512(clientSecret),
                registerUserResponse
            );

            //when
            //then
            assertThatThrownBy(() -> tokenProvider.validateToken(expiredToken))
                .isInstanceOf(InvalidJwtException.class);
        }
    }
}
