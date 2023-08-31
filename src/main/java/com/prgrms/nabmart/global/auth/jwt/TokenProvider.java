package com.prgrms.nabmart.global.auth.jwt;

import com.prgrms.nabmart.domain.user.service.response.RegisterUserResponse;

public interface TokenProvider {

    String createToken(RegisterUserResponse userResponse);
}
