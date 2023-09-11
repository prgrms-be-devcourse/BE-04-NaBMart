package com.prgrms.nabmart.domain.user;

import lombok.Getter;

import java.util.List;

@Getter
public enum UserRole {
    ROLE_USER(Constants.ROLE_USER, List.of(Constants.ROLE_USER)),
    ROLE_RIDER(Constants.ROLE_RIDER, List.of(Constants.ROLE_RIDER)),
    ROLE_ADMIN(Constants.ROLE_ADMIN, List.of(Constants.ROLE_ADMIN, Constants.ROLE_USER));

    private final String value;
    private final List<String> authorities;

    UserRole(String value, List<String> authorities) {
        this.value = value;
        this.authorities = authorities;
    }

    private static class Constants {
        private static final String ROLE_USER = "ROLE_USER";
        private static final String ROLE_RIDER = "ROLE_RIDER";
        private static final String ROLE_ADMIN = "ROLE_ADMIN";
    }
}
