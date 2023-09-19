package com.prgrms.nabmart.global.auth.oauth.client;

import static com.prgrms.nabmart.global.auth.oauth.constant.OAuthConstant.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.global.auth.exception.OAuthUnlinkFailureException;
import com.prgrms.nabmart.global.auth.oauth.dto.OAuthHttpMessage;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class NaverMessageProvider implements OAuthHttpMessageProvider {

    private static final String UNLINK_URI = "https://nid.naver.com/oauth2.0/token?"
        + "client_id={client_id}&client_secret={client_secret}&access_token={access_token}&"
        + "grant_type={grant_type}&service_provider={service_provider}";
    private static final String REFRESH_ACCESS_TOKEN_URI = "https://nid.naver.com/oauth2.0/token?"
        + "grant_type=refresh_token&client_id={client_id}&"
        + "client_secret={client_secret}&refresh_token={refresh_token}";

    @Override
    public OAuthHttpMessage createUserUnlinkRequest(
        final FindUserDetailResponse userDetailResponse,
        final OAuth2AuthorizedClient authorizedClient) {
        String accessToken = getAccessToken(authorizedClient);
        HttpEntity<MultiValueMap<String, String>> unlinkHttpMessage = createUnlinkOAuthUserMessage();
        Map<String, String> unlinkUriVariables = createUnlinkUriVariables(
            authorizedClient.getClientRegistration(), accessToken);
        return new OAuthHttpMessage(UNLINK_URI, unlinkHttpMessage, unlinkUriVariables);
    }

    private String getAccessToken(final OAuth2AuthorizedClient authorizedClient) {
        return authorizedClient.getAccessToken().getTokenValue();
    }

    private HttpEntity<MultiValueMap<String, String>> createUnlinkOAuthUserMessage() {
        HttpHeaders header = createHeader();
        return new HttpEntity<>(header);
    }

    private HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private Map<String, String> createUnlinkUriVariables(
        final ClientRegistration clientRegistration,
        final String accessToken) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put(CLIENT_ID, clientRegistration.getClientId());
        urlVariables.put(CLIENT_SECRET, clientRegistration.getClientSecret());
        urlVariables.put(ACCESS_TOKEN, accessToken);
        urlVariables.put(GRANT_TYPE, DELETE);
        urlVariables.put(SERVICE_PROVIDER, NAVER);
        return urlVariables;
    }

    @Override
    public void checkSuccessUnlinkRequest(Map<String, Object> unlinkResponse) {
        Optional.ofNullable(unlinkResponse.get(RESULT))
            .filter(result -> result.equals(SUCCESS))
            .orElseThrow(() -> new OAuthUnlinkFailureException("소셜 로그인 연동 해제가 실패하였습니다."));
    }

    @Override
    public OAuthHttpMessage createRefreshAccessTokenRequest(
        OAuth2AuthorizedClient authorizedClient) {
        return new OAuthHttpMessage(
            REFRESH_ACCESS_TOKEN_URI,
            createEmptyMessage(),
            createRefreshAccessTokenUriVariables(authorizedClient));
    }

    private HttpEntity<MultiValueMap<String, String>> createEmptyMessage() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        return new HttpEntity<>(map, map);
    }

    private Map<String, String> createRefreshAccessTokenUriVariables(
        OAuth2AuthorizedClient authorizedClient) {
        Map<String, String> variables = new HashMap<>();
        variables.put(CLIENT_ID, authorizedClient.getClientRegistration().getClientId());
        variables.put(CLIENT_SECRET, authorizedClient.getClientRegistration().getClientSecret());
        variables.put(REFRESH_TOKEN, authorizedClient.getRefreshToken().getTokenValue());
        variables.put(GRANT_TYPE, REFRESH_TOKEN);
        return variables;
    }

    @Override
    public OAuth2AccessToken extractAccessToken(Map response) {
        String accessToken = (String) response.get(ACCESS_TOKEN);
        String expiresInSeconds = (String) response.get(EXPIRES_IN);
        Instant now = Instant.now();
        Instant expiresIn = now.plusSeconds(Long.parseLong(expiresInSeconds));
        return new OAuth2AccessToken(TokenType.BEARER, accessToken, now, expiresIn);
    }

    @Override
    public Optional<OAuth2RefreshToken> extractRefreshToken(Map response) {
        String refreshToken = (String) response.get(REFRESH_TOKEN);
        return Optional.ofNullable(refreshToken)
            .map(token -> new OAuth2RefreshToken(token, Instant.now()));
    }
}
