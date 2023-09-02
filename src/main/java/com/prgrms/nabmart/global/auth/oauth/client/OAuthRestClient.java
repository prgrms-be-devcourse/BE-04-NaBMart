package com.prgrms.nabmart.global.auth.oauth.client;

import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;

public interface OAuthRestClient {

    String callUnlinkOAuthUser(FindUserDetailResponse userDetailResponse);
}
