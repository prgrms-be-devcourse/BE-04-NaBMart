package com.prgrms.nabmart.global.auth.oauth.client;

import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.global.auth.oauth.dto.OAuthHttpMessage;
import com.prgrms.nabmart.global.auth.oauth.handler.OAuthProvider;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
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
        OAuthHttpMessage unlinkHttpMessage = oAuthHttpMessageProvider.createUnlinkHttpMessage(
            userDetailResponse, oAuth2AuthorizedClient);
        Map<String, Object> response = restTemplate.postForObject(
            unlinkHttpMessage.uri(),
            unlinkHttpMessage.httpMessage(),
            Map.class,
            unlinkHttpMessage.uriVariables());
        log.info("회원의 연결이 종료되었습니다. 회원 ID={}", response);
        oAuthHttpMessageProvider.checkSuccessUnlinkRequest(response);
    }

}
