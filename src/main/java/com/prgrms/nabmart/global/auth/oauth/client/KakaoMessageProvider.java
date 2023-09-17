package com.prgrms.nabmart.global.auth.oauth.client;

import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.global.auth.exception.OAuthUnlinkFailureException;
import com.prgrms.nabmart.global.auth.oauth.dto.OAuthHttpMessage;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String AUTHORIZATION = "Authorization";
    private static final String TARGET_ID_TYPE = "target_id_type";
    private static final String USER_ID = "user_id";
    private static final String TARGET_ID = "target_id";
    private static final String ID = "id";
    private static final String APPLICATION_X_WWW_FORM_URLENCODED_CHARSET_UTF_8
        = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String GRANT_TYPE = "grant_type";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String EXPIRES_IN = "expires_in";

    @Override
    public OAuthHttpMessage createUserUnlinkRequest(
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
        headers.set(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.set(AUTHORIZATION, MessageFormat.format("Bearer {0}", accessToken));
        return headers;
    }

    private MultiValueMap<String, String> createUnlinkMessageBody(
        final FindUserDetailResponse userDetailResponse) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add(TARGET_ID_TYPE, USER_ID);
        multiValueMap.add(TARGET_ID, String.valueOf(userDetailResponse.providerId()));
        return multiValueMap;
    }

    @Override
    public void checkSuccessUnlinkRequest(Map<String, Object> unlinkResponse) {
        Optional.ofNullable(unlinkResponse.get(ID))
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
        headers.add(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED_CHARSET_UTF_8);
        return headers;
    }

    private MultiValueMap<String, String> createRefreshAccessTokenMessageBody(
        OAuth2AuthorizedClient authorizedClient) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(GRANT_TYPE, REFRESH_TOKEN);
        params.add(CLIENT_ID, authorizedClient.getClientRegistration().getClientId());
        params.add(REFRESH_TOKEN, authorizedClient.getRefreshToken().getTokenValue());
        params.add(CLIENT_SECRET, authorizedClient.getClientRegistration().getClientSecret());
        return params;
    }

    @Override
    public OAuth2AccessToken extractAccessToken(Map response) {
        String accessToken = (String) response.get(ACCESS_TOKEN);
        Instant now = Instant.now();
        Integer expiresInSeconds = (Integer) response.get(EXPIRES_IN);
        Instant expiresIn = now.plusSeconds(expiresInSeconds);
        return new OAuth2AccessToken(TokenType.BEARER, accessToken, now, expiresIn);
    }

    @Override
    public Optional<OAuth2RefreshToken> extractRefreshToken(Map response) {
        String refreshToken = (String) response.get(REFRESH_TOKEN);
        return Optional.ofNullable(refreshToken)
            .map(token -> new OAuth2RefreshToken(token, Instant.now()));
    }
}
