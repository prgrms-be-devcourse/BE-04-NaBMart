package com.prgrms.nabmart.domain.user.service.request;

import com.prgrms.nabmart.domain.user.UserRole;
import lombok.Builder;

@Builder
public record RegisterUserCommand(
        String nickname,
        String provider,
        String providerId,
        UserRole userRole) {

}
