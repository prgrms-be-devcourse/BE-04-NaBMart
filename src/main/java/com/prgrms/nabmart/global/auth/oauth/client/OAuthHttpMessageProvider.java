package com.prgrms.nabmart.global.auth.oauth.client;

import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.global.auth.oauth.dto.OAuthHttpMessage;
import java.util.Map;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

public interface OAuthHttpMessageProvider {

    OAuthHttpMessage createUnlinkHttpMessage(
        final FindUserDetailResponse userDetailResponse,
        final OAuth2AuthorizedClient authorizedClient);

    void checkSuccessUnlinkRequest(Map<String, Object> unlinkResponse);
}
