package com.prgrms.nabmart.domain.user.service.request;

import com.prgrms.nabmart.domain.user.UserRole;
import lombok.Builder;

@Builder
public record RegisterOAuthUserCommnad(
        String nickname,
        String provider,
        String providerId,
        UserRole userRole) {

    public static RegisterOAuthUserCommnad of(
            String nickname,
            String provider,
            String providerId,
            UserRole userRole) {
        return RegisterOAuthUserCommnad.builder()
                .nickname(nickname)
                .provider(provider)
                .providerId(providerId)
                .userRole(userRole)
                .build();
    }
}
