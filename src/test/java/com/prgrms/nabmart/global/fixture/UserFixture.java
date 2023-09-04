package com.prgrms.nabmart.global.fixture;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserGrade;
import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.service.request.RegisterUserCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserFixture {

    private static final String NICKNAME = "닉네임";
    private static final String EMAIL = "email@example.com";
    private static final String PROVIDER = "provider";
    private static final String PROVIDER_ID = "providerId";
    private static final UserRole USER_ROLE = UserRole.ROLE_USER;
    private static final UserGrade USER_GRADE = UserGrade.NORMAL;

    public static User user() {

        return User.builder()
            .nickname(NICKNAME)
            .email(EMAIL)
            .provider(PROVIDER)
            .providerId(PROVIDER_ID)
            .userRole(USER_ROLE)
            .userGrade(USER_GRADE)
            .build();
    }

    public static RegisterUserCommand registerUserCommand() {
        return RegisterUserCommand.builder()
            .nickname(NICKNAME)
            .email(EMAIL)
            .provider(PROVIDER)
            .providerId(PROVIDER_ID)
            .userRole(USER_ROLE)
            .userGrade(USER_GRADE)
            .build();
    }
}
