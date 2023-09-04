package com.prgrms.nabmart.global.auth.jwt;

import com.prgrms.nabmart.domain.user.service.response.RegisterUserResponse;
import com.prgrms.nabmart.global.auth.jwt.dto.Claims;

public interface TokenProvider {

    String createToken(final RegisterUserResponse userResponse);

    Claims validateToken(final String accessToken);
}
