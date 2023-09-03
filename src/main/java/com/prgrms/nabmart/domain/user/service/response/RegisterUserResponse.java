package com.prgrms.nabmart.domain.user.service.response;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserRole;

public record RegisterUserResponse(Long userId, UserRole userRole) {

    public static RegisterUserResponse from(User user) {
        return new RegisterUserResponse(
            user.getUserId(),
            user.getUserRole()
        );
    }
}
