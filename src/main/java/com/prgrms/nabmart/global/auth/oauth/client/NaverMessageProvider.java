package com.prgrms.nabmart.global.auth.oauth.client;

import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.global.auth.oauth.dto.OAuthHttpMessage;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.util.MultiValueMap;

public class NaverMessageProvider implements OAuthHttpMessageProvider {

    private static final String UNLINK_URI = "https://nid.naver.com/oauth2.0/token?"
        + "client_id={client_id}&client_secret={client_secret}&access_token={access_token}&"
        + "grant_type={grant_type}&service_provider={service_provider}";

    @Override
    public OAuthHttpMessage createUnlinkHttpMessage(
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
        headers.set("Content-Type", "application/json");
        return headers;
    }

    private Map<String, String> createUnlinkUriVariables(
        final ClientRegistration clientRegistration,
        final String accessToken) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("client_id", clientRegistration.getClientId());
        urlVariables.put("client_secret", clientRegistration.getClientSecret());
        urlVariables.put("access_token", accessToken);
        urlVariables.put("grant_type", "delete");
        urlVariables.put("service_provider", "NAVER");
        return urlVariables;
    }
}
