package com.prgrms.nabmart.domain.user.service.response;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserRole;

public record RegisterUserResponse(
    Long userId,
    String nickname,
    String providerId,
    UserRole userRole) {

    public static RegisterUserResponse from(final User user) {
        return new RegisterUserResponse(
            user.getUserId(),
            user.getNickname(),
            user.getProviderId(),
            user.getUserRole()
        );
    }
}
