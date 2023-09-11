package com.prgrms.nabmart.domain.delivery.support;

import com.prgrms.nabmart.domain.delivery.service.request.RiderSignupCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RiderFixture {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ADDRESS = "address";

    public static RiderSignupCommand riderSignupCommand() {
        return new RiderSignupCommand(USERNAME, PASSWORD, ADDRESS);
    }
}
