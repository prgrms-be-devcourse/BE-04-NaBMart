package com.prgrms.nabmart.global.auth.support;

import com.prgrms.nabmart.domain.delivery.Rider;
import com.prgrms.nabmart.global.auth.controller.request.LoginRiderRequest;
import com.prgrms.nabmart.global.auth.service.request.LoginRiderCommand;
import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.service.response.RegisterUserResponse;
import com.prgrms.nabmart.global.auth.jwt.JavaJwtTokenProvider;
import com.prgrms.nabmart.global.auth.jwt.TokenProvider;
import com.prgrms.nabmart.global.auth.jwt.dto.CreateTokenCommand;
import com.prgrms.nabmart.global.auth.jwt.dto.JwtAuthentication;
import com.prgrms.nabmart.global.auth.service.request.SignupRiderCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthFixture {

    private static final String ISSUER = "issuer";
    private static final String CLIENT_SECRET = "thisIsClientSecretForTestNotARealSecretSoDontWorry";
    private static final int EXPIRY_SECONDS = 60;
    private static final Long USER_ID = 1L;
    private static final String RIDER_USERNAME = "rider123";
    private static final String RIDER_PASSWORD = "rider123";
    private static final String RIDER_ADDRESS = "address";

    public static TokenProvider tokenProvider() {
        return new JavaJwtTokenProvider(ISSUER, CLIENT_SECRET, EXPIRY_SECONDS);
    }

    public static Authentication usernamePasswordAuthenticationToken() {
        JwtAuthentication authentication = new JwtAuthentication(USER_ID, accessToken());
        return new UsernamePasswordAuthenticationToken(
            authentication,
            null,
            UserRole.ROLE_USER.getAuthorities().stream().map(SimpleGrantedAuthority::new).toList());
    }

    public static RegisterUserResponse registerUserResponse() {
        return new RegisterUserResponse(1L, "nickname", "abc", UserRole.ROLE_USER);
    }

    public static CreateTokenCommand createTokenCommand() {
        return new CreateTokenCommand(USER_ID, UserRole.ROLE_USER);
    }

    public static String accessToken() {
        TokenProvider tokenProvider = tokenProvider();
        return tokenProvider.createToken(createTokenCommand());
    }

    public static LoginRiderCommand riderLoginCommand() {
        return new LoginRiderCommand(RIDER_USERNAME, RIDER_PASSWORD);
    }

    public static LoginRiderRequest riderLoginRequest() {
        return new LoginRiderRequest(RIDER_USERNAME, RIDER_PASSWORD);
    }

    public static Rider rider() {
        String encodePassword = mockPasswordEncoder().encode(RIDER_PASSWORD);
        return new Rider(RIDER_USERNAME, encodePassword, RIDER_ADDRESS);
    }

    public static SignupRiderCommand riderSignupCommand() {
        return new SignupRiderCommand(RIDER_USERNAME, RIDER_PASSWORD, RIDER_ADDRESS);
    }

    public static PasswordEncoder mockPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return new StringBuilder(rawPassword).reverse().toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encode(rawPassword).equals(encodedPassword);
            }
        };
    }
}
