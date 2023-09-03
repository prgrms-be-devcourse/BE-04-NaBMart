package com.prgrms.nabmart.domain.user.service.request;

import com.prgrms.nabmart.domain.user.UserRole;
import lombok.Builder;

@Builder
public record RegisterOAuthUserCommand(
    String nickname,
    String email,
    String provider,
    String providerId,
    UserRole userRole) {

    public static RegisterOAuthUserCommand of(
        String nickname,
        String email,
        String provider,
        String providerId,
        UserRole userRole) {
        return RegisterOAuthUserCommand.builder()
            .nickname(nickname)
            .email(email)
            .provider(provider)
            .providerId(providerId)
            .userRole(userRole)
            .build();
    }
}
