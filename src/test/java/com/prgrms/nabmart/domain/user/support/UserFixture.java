package com.prgrms.nabmart.domain.user.support;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserGrade;
import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.service.request.FindUserCommand;
import com.prgrms.nabmart.domain.user.service.request.RegisterUserCommand;
import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import org.springframework.test.util.ReflectionTestUtils;

public class UserFixture {

    private static final Long USER_ID = 1L;
    private static final String NICKNAME = "닉네임";
    private static final String EMAIL = "email@example.com";
    private static final String PROVIDER = "provider";
    private static final String PROVIDER_ID = "providerId";

    private static final String ADDRESS = "기본 배송지";
    private static final UserRole USER_ROLE = UserRole.ROLE_USER;
    private static final UserGrade USER_GRADE = UserGrade.NORMAL;
    private static final UserRole EMPLOYEE = UserRole.ROLE_EMPLOYEE;

    public static User user() {
        return User.builder()
            .nickname(NICKNAME)
            .email(EMAIL)
            .provider(PROVIDER)
            .providerId(PROVIDER_ID)
            .userRole(USER_ROLE)
            .userGrade(USER_GRADE)
            .address(ADDRESS)
            .build();
    }

    public static User userWithUserId() {
        User user = User.builder()
            .nickname(NICKNAME)
            .email(EMAIL)
            .provider(PROVIDER)
            .providerId(PROVIDER_ID)
            .userRole(USER_ROLE)
            .userGrade(USER_GRADE)
            .address(ADDRESS)
            .build();

        ReflectionTestUtils.setField(user, "userId", USER_ID);
        return user;
    }

    public static User employee() {
        return User.builder()
            .nickname(NICKNAME)
            .email(EMAIL)
            .provider(PROVIDER)
            .providerId(PROVIDER_ID)
            .userRole(EMPLOYEE)
            .userGrade(USER_GRADE)
            .address(ADDRESS)
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

    public static FindUserDetailResponse findUserDetailResponse() {
        return new FindUserDetailResponse(
            USER_ID,
            NICKNAME,
            EMAIL,
            PROVIDER,
            PROVIDER_ID,
            USER_ROLE,
            USER_GRADE);
    }

    public static FindUserCommand findUserCommand() {
        return FindUserCommand.from(USER_ID);
    }
}
