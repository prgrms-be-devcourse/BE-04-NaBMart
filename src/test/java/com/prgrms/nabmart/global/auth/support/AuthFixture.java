package com.prgrms.nabmart.global.auth.support;

import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.service.response.RegisterUserResponse;
import com.prgrms.nabmart.global.auth.jwt.JavaJwtTokenProvider;
import com.prgrms.nabmart.global.auth.jwt.TokenProvider;
import com.prgrms.nabmart.global.auth.jwt.dto.JwtAuthentication;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthFixture {

    private static final String ISSUER = "issuer";
    private static final String CLIENT_SECRET = "thisIsClientSecretForTestNotARealSecretSoDontWorry";
    private static final int EXPIRY_SECONDS = 60;
    private static final Long USER_ID = 1L;
    private static final String TOKEN = "token";

    public static TokenProvider tokenProvider() {
        return new JavaJwtTokenProvider(ISSUER, CLIENT_SECRET, EXPIRY_SECONDS);
    }

    public static Authentication usernamePasswordAuthenticationToken() {
        JwtAuthentication authentication = new JwtAuthentication(USER_ID, TOKEN);
        return new UsernamePasswordAuthenticationToken(authentication, null);
    }

    public static RegisterUserResponse registerUserResponse() {
        return new RegisterUserResponse(1L, "nickname", "abc", UserRole.ROLE_USER);
    }

    public static String accessToken() {
        RegisterUserResponse userResponse = registerUserResponse();
        TokenProvider tokenProvider = tokenProvider();
        return tokenProvider.createToken(userResponse);
    }
}