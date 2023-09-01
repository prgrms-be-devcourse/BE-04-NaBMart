package com.prgrms.nabmart.global.fixture;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserRole;

public final class UserFixture {

    private static final String NICKNAME = "닉네임";
    private static final String PROVIDER = "provider";
    private static final String PROVIDER_ID = "providerId";
    private static final UserRole USER_ROLE = UserRole.ROLE_USER;

    private UserFixture() {
    }

    public static User user() {
        return User.builder()
            .nickname(NICKNAME)
            .provider(PROVIDER)
            .providerId(PROVIDER_ID)
            .userRole(UserRole.ROLE_USER)
            .build();
    }
}
