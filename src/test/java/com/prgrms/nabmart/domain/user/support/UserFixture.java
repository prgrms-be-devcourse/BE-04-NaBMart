package com.prgrms.nabmart.domain.user.support;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserRole;

public class UserFixture {

    private static final String NICKNAME = "nickname";
    private static final String EMAIL = "email@example.com";
    private static final String PROVIDER = "nayb";
    private static final String PROVIDER_ID = "123123";
    private static final UserRole USER_ROLE = UserRole.ROLE_USER;

    public static User getUser(long userId) {
        return User.builder()
                .userId(userId)
                .userRole(USER_ROLE)
                .nickname(NICKNAME)
                .email(EMAIL)
                .provider(PROVIDER)
                .providerId(PROVIDER_ID)
                .build();
    }
}
