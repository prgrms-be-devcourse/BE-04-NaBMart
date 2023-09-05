package com.prgrms.nabmart.global.auth.oauth.client;

import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.global.auth.exception.OAuthUnlinkFailureException;
import com.prgrms.nabmart.global.auth.oauth.dto.OAuthHttpMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RequiredArgsConstructor
public class KakaoMessageProvider implements OAuthHttpMessageProvider {

    private static final String UNLINK_URI = "https://kapi.kakao.com/v1/user/unlink";

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
}
