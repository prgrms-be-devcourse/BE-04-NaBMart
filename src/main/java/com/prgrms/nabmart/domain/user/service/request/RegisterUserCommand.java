package com.prgrms.nabmart.domain.user.service.request;

import com.prgrms.nabmart.domain.user.UserRole;
import lombok.Builder;

@Builder
public record RegisterUserCommand(
    String nickname,
    String email,
    String provider,
    String providerId,
    UserRole userRole) {

    public static RegisterUserCommand of(
        String nickname,
        String email,
        String provider,
        String providerId,
        UserRole userRole) {
        return RegisterUserCommand.builder()
            .nickname(nickname)
            .email(email)
            .provider(provider)
            .providerId(providerId)
            .userRole(userRole)
            .build();
    }
}
