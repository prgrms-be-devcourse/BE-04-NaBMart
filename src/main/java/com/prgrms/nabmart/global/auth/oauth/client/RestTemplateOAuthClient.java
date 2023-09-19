package com.prgrms.nabmart.global.auth.oauth.client;

import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.global.auth.oauth.dto.OAuthHttpMessage;
import com.prgrms.nabmart.global.auth.oauth.OAuthProvider;
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

        refreshAccessTokenIfNotValid(userDetailResponse, oAuth2AuthorizedClient);

        OAuthHttpMessage unlinkHttpMessage = oAuthHttpMessageProvider.createUnlinkUserRequest(
            userDetailResponse, oAuth2AuthorizedClient);

        Map<String, Object> response = sendPostApiRequest(unlinkHttpMessage);
        log.info("회원의 연결이 종료되었습니다. 회원 ID={}", response);

        oAuthHttpMessageProvider.verifySuccessUnlinkUserRequest(response);
        authorizedClientService.removeAuthorizedClient(
            userDetailResponse.provider(),
            userDetailResponse.provider());
    }

    private void refreshAccessTokenIfNotValid(FindUserDetailResponse userDetailResponse,
        OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        Instant expiresAt = oAuth2AuthorizedClient.getAccessToken().getExpiresAt();
        if(expiresAt.isBefore(Instant.now())) {
            callRefreshAccessToken(userDetailResponse);
        }
    }

    @Override
    public void callRefreshAccessToken(final FindUserDetailResponse userDetailResponse) {
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
        Authentication authenticationForTokenRefresh
            = getAuthenticationForTokenRefresh(updatedAuthorizedClient);
        authorizedClientService.saveAuthorizedClient(
            updatedAuthorizedClient,
            authenticationForTokenRefresh);
    }

    private Authentication getAuthenticationForTokenRefresh(
        OAuth2AuthorizedClient updatedAuthorizedClient) {
        String principalName = updatedAuthorizedClient.getPrincipalName();
        return UsernamePasswordAuthenticationToken.authenticated(
            principalName, null, List.of());
    }

    private Map sendPostApiRequest(OAuthHttpMessage request) {
        return restTemplate.postForObject(
            request.uri(),
            request.httpMessage(),
            Map.class,
            request.uriVariables());
    }
}
