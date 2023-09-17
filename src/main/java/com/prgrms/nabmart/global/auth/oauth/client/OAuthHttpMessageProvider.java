package com.prgrms.nabmart.global.auth.oauth.client;

import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.global.auth.oauth.dto.OAuthHttpMessage;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

public interface OAuthHttpMessageProvider {

    OAuthHttpMessage createUnlinkHttpMessage(
        final FindUserDetailResponse userDetailResponse,
        final OAuth2AuthorizedClient authorizedClient);

    void checkSuccessUnlinkRequest(Map<String, Object> unlinkResponse);

    OAuthHttpMessage createRefreshAccessTokenRequest(OAuth2AuthorizedClient authorizedClient);

    OAuth2AccessToken extractAccessToken(Map response);

    Optional<OAuth2RefreshToken> extractRefreshToken(Map response);
}
