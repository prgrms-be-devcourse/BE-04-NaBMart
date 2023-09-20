package com.prgrms.nabmart.global.auth.oauth.client;

import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;

public interface OAuthRestClient {

    void callUnlinkOAuthUser(FindUserDetailResponse userDetailResponse);

    void callRefreshAccessToken(FindUserDetailResponse userDetailResponse);
}
