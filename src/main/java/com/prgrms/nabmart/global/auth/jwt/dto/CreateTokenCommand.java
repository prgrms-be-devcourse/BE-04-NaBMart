package com.prgrms.nabmart.global.auth.jwt.dto;

import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.service.response.RegisterUserResponse;

public record CreateTokenCommand(Long userId, UserRole userRole) {

    public static CreateTokenCommand from(RegisterUserResponse userResponse) {
        return new CreateTokenCommand(userResponse.userId(), userResponse.userRole());
    }
}
