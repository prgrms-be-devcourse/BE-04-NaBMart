package com.prgrms.nabmart.global.auth.oauth.client;

import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.global.auth.oauth.dto.OAuthHttpMessage;
import com.prgrms.nabmart.global.auth.oauth.handler.OAuthProvider;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RestTemplateOAuthClient implements OAuthRestClient {

    private final RestTemplate restTemplate;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public RestTemplateOAuthClient(final OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void callUnlinkOAuthUser(final FindUserDetailResponse userDetailResponse) {
        OAuthProvider oAuthProvider = OAuthProvider.getOAuthProvider(userDetailResponse.provider());
        OAuthHttpMessageProvider oAuthHttpMessageProvider = oAuthProvider.getOAuthHttpMessageProvider();
        OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService.loadAuthorizedClient(
            userDetailResponse.provider(),
            userDetailResponse.providerId());

        Instant expiresAt = oAuth2AuthorizedClient.getAccessToken().getExpiresAt();
        if(expiresAt.isBefore(Instant.now())) {
            refreshAccessToken(userDetailResponse);
        }

        OAuthHttpMessage unlinkHttpMessage = oAuthHttpMessageProvider.createUserUnlinkRequest(
            userDetailResponse, oAuth2AuthorizedClient);
        Map<String, Object> response = sendPostApiRequest(unlinkHttpMessage);
        log.info("회원의 연결이 종료되었습니다. 회원 ID={}", response);
        oAuthHttpMessageProvider.checkSuccessUnlinkRequest(response);
    }

    @Override
    public void refreshAccessToken(final FindUserDetailResponse userDetailResponse) {
        OAuthProvider oAuthProvider = OAuthProvider.getOAuthProvider(userDetailResponse.provider());
        OAuthHttpMessageProvider oAuthHttpMessageProvider = oAuthProvider.getOAuthHttpMessageProvider();
        OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService.loadAuthorizedClient(
            userDetailResponse.provider(),
            userDetailResponse.providerId());
        OAuthHttpMessage refreshAccessTokenRequest
            = oAuthHttpMessageProvider.createRefreshAccessTokenRequest(oAuth2AuthorizedClient);

        Map response = sendPostApiRequest(refreshAccessTokenRequest);

        OAuth2AccessToken refreshedAccessToken
            = oAuthHttpMessageProvider.extractAccessToken(response);
        OAuth2RefreshToken refreshedRefreshToken
            = oAuthHttpMessageProvider.extractRefreshToken(response)
            .orElse(oAuth2AuthorizedClient.getRefreshToken());

        OAuth2AuthorizedClient updatedAuthorizedClient = new OAuth2AuthorizedClient(
            oAuth2AuthorizedClient.getClientRegistration(),
            oAuth2AuthorizedClient.getPrincipalName(),
            refreshedAccessToken,
            refreshedRefreshToken);
        String principalName = updatedAuthorizedClient.getPrincipalName();
        Authentication authenticationForTokenRefresh
            = getAuthenticationForTokenRefresh(principalName);
        authorizedClientService.saveAuthorizedClient(
            updatedAuthorizedClient,
            authenticationForTokenRefresh);
    }

    private Authentication getAuthenticationForTokenRefresh(String principalName) {
        return UsernamePasswordAuthenticationToken.authenticated(
            principalName, null, List.of());
    }

    private Map sendPostApiRequest(OAuthHttpMessage refreshAccessTokenRequest) {
        return restTemplate.postForObject(
            refreshAccessTokenRequest.uri(),
            refreshAccessTokenRequest.httpMessage(),
            Map.class,
            refreshAccessTokenRequest.uriVariables());
    }
}
