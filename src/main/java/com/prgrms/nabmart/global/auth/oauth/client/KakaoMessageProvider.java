package com.prgrms.nabmart.global.auth.oauth.client;

import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.global.auth.exception.OAuthUnlinkFailureException;
import com.prgrms.nabmart.global.auth.oauth.dto.OAuthHttpMessage;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RequiredArgsConstructor
public class KakaoMessageProvider implements OAuthHttpMessageProvider {

    private static final String UNLINK_URI = "https://kapi.kakao.com/v1/user/unlink";
    private static final String ACCESS_TOKEN_REFRESH_URI = "https://kauth.kakao.com/oauth/token";

    @Override
    public OAuthHttpMessage createUnlinkHttpMessage(
        final FindUserDetailResponse userDetailResponse,
        final OAuth2AuthorizedClient authorizedClient) {
        String accessToken = getAccessToken(authorizedClient);
        HttpEntity<MultiValueMap<String, String>> unlinkHttpMeesage = createUnlinkOAuthUserMessage(
            userDetailResponse, accessToken);
        return new OAuthHttpMessage(UNLINK_URI, unlinkHttpMeesage, new HashMap<>());
    }

    private String getAccessToken(final OAuth2AuthorizedClient authorizedClient) {
        return authorizedClient.getAccessToken().getTokenValue();
    }

    private HttpEntity<MultiValueMap<String, String>> createUnlinkOAuthUserMessage(
        final FindUserDetailResponse userDetailResponse,
        final String accessToken) {
        HttpHeaders headers = createHeader(accessToken);
        MultiValueMap<String, String> multiValueMap = createUnlinkMessageBody(userDetailResponse);
        return new HttpEntity<>(multiValueMap, headers);
    }

    private HttpHeaders createHeader(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }

    private MultiValueMap<String, String> createUnlinkMessageBody(
        final FindUserDetailResponse userDetailResponse) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("target_id_type", "user_id");
        multiValueMap.add("target_id", String.valueOf(userDetailResponse.providerId()));
        return multiValueMap;
    }

    @Override
    public void checkSuccessUnlinkRequest(Map<String, Object> unlinkResponse) {
        Optional.ofNullable(unlinkResponse.get("id"))
            .orElseThrow(() -> new OAuthUnlinkFailureException("소셜 로그인 연동 해제가 실패하였습니다."));
    }

    @Override
    public OAuthHttpMessage createRefreshAccessTokenRequest(OAuth2AuthorizedClient authorizedClient) {
        return new OAuthHttpMessage(
            ACCESS_TOKEN_REFRESH_URI,
            createRefreshAccessTokenMessage(authorizedClient),
            new HashMap<>());
    }

    private HttpEntity<MultiValueMap<String, String>> createRefreshAccessTokenMessage(
        OAuth2AuthorizedClient authorizedClient) {
        return new HttpEntity<>(
            createRefreshAccessTokenMessageBody(authorizedClient),
            createRefreshAccessTokenMessageHeader());
    }

    private MultiValueMap<String, String> createRefreshAccessTokenMessageHeader() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content_type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    private MultiValueMap<String, String> createRefreshAccessTokenMessageBody(
        OAuth2AuthorizedClient authorizedClient) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("client_id", authorizedClient.getClientRegistration().getClientId());
        params.add("refresh_token", authorizedClient.getRefreshToken().getTokenValue());
        params.add("client_secret", authorizedClient.getClientRegistration().getClientSecret());
        return params;
    }

    @Override
    public OAuth2AccessToken extractAccessToken(Map response) {
        String accessToken = (String) response.get("access_token");
        Instant now = Instant.now();
        Integer expiresInSeconds = (Integer) response.get("expires_in");
        Instant expiresIn = now.plusSeconds(expiresInSeconds);
        return new OAuth2AccessToken(TokenType.BEARER, accessToken, now, expiresIn);
    }

    @Override
    public Optional<OAuth2RefreshToken> extractRefreshToken(Map response) {
        String refreshToken = (String) response.get("refresh_token");
        return Optional.ofNullable(refreshToken)
            .map(token -> new OAuth2RefreshToken(token, Instant.now()));
    }
}
