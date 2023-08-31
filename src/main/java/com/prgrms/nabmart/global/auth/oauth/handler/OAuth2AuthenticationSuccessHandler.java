package com.prgrms.nabmart.global.auth.oauth.handler;

import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.service.UserService;
import com.prgrms.nabmart.domain.user.service.request.RegisterUserCommand;
import com.prgrms.nabmart.domain.user.service.response.RegisterUserResponse;
import com.prgrms.nabmart.global.auth.jwt.TokenProvider;
import com.prgrms.nabmart.global.auth.oauth.dto.OAuthUserInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
            String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            OAuthUserInfo oAuthUserInfo = OAuthProvider.getOAuthProvider(registrationId)
                    .getNickname(oAuth2User.getAttributes());

            RegisterUserCommand registerUserCommand = RegisterUserCommand.of(
                    oAuthUserInfo.nickname(),
                    registrationId,
                    oAuthUserInfo.oAuthUserId(),
                    UserRole.ROLE_USER);
            RegisterUserResponse user = userService.getOrRegisterUser(registerUserCommand);
            String accessToken = tokenProvider.createToken(user);


        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
