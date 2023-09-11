package com.prgrms.nabmart.global.auth.jwt;

import com.prgrms.nabmart.global.auth.jwt.dto.Claims;
import com.prgrms.nabmart.global.auth.jwt.dto.CreateTokenCommand;

public interface TokenProvider {

    String createToken(final CreateTokenCommand createTokenCommand);

    Claims validateToken(final String accessToken);
}
