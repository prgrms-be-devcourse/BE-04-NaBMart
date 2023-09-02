package com.prgrms.nabmart.global.auth.oauth.client;

import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RestTemplateOAuthClient implements OAuthRestClient {

    private final RestTemplate restTemplate;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public RestTemplateOAuthClient(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String callUnlinkOAuthUser(FindUserDetailResponse userDetailResponse) {
        String accessToken = getAccessToken(userDetailResponse);
        HttpEntity<MultiValueMap<String, String>> entity = createHttpRequestMessage(
            userDetailResponse,
            accessToken);
        KakaoUnlinkResponse kakaoUnlinkResponse = restTemplate.postForObject(
            "https://kapi.kakao.com/v1/user/unlink",
            entity,
            KakaoUnlinkResponse.class);
        log.info("회원의 연결이 종료되었습니다. 회원 ID={}", kakaoUnlinkResponse);
        return null;
    }

    private String getAccessToken(FindUserDetailResponse userDetailResponse) {
        OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService.loadAuthorizedClient(
            userDetailResponse.provider(),
            userDetailResponse.providerId());
        OAuth2AccessToken oAuth2AccessToken = oAuth2AuthorizedClient.getAccessToken();
        return oAuth2AccessToken.getTokenValue();
    }

    private HttpEntity<MultiValueMap<String, String>> createHttpRequestMessage(
        FindUserDetailResponse userDetailResponse,
        String accessToken) {
        HttpHeaders headers = createHeader(accessToken);
        MultiValueMap<String, String> multiValueMap = createMessageBody(userDetailResponse);
        return new HttpEntity<>(multiValueMap, headers);
    }

    private HttpHeaders createHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }

    private MultiValueMap<String, String> createMessageBody(
        FindUserDetailResponse userDetailResponse) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("target_id_type", "user_id");
        multiValueMap.add("target_id", String.valueOf(userDetailResponse.providerId()));
        return multiValueMap;
    }
}
