package com.prgrms.nabmart.domain.user.service.response;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserRole;

public record AuthUserResponse(
    Long userId,
    String nickname,
    String providerId,
    UserRole userRole) {

    public static AuthUserResponse from(final User user) {
        return new AuthUserResponse(
            user.getUserId(),
            user.getNickname(),
            user.getProviderId(),
            user.getUserRole()
        );
    }
}
