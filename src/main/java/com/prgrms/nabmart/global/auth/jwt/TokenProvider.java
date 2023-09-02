package com.prgrms.nabmart.global.auth.jwt;

import com.prgrms.nabmart.domain.user.service.response.AuthUserResponse;
import com.prgrms.nabmart.global.auth.jwt.dto.Claims;

public interface TokenProvider {

    String createToken(final AuthUserResponse userResponse);

    Claims validateToken(final String accessToken);
}
