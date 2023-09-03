package com.prgrms.nabmart.domain.user.service.response;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserRole;

public record FindUserDetailResponse(
    Long userId,
    String nickname,
    String email,
    String provider,
    String providerId,
    UserRole userRole) {

    public static FindUserDetailResponse from(final User findUser) {
        return new FindUserDetailResponse(
            findUser.getUserId(),
            findUser.getNickname(),
            findUser.getEmail(),
            findUser.getProvider(),
            findUser.getProviderId(),
            findUser.getUserRole());
    }
}
