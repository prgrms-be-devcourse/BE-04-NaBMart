package com.prgrms.nabmart.global.auth.service.request;

public record SignupRiderCommand(String username, String password, String address) {

    public static SignupRiderCommand of(
        final String username,
        final String password,
        final String address) {
        return new SignupRiderCommand(username, password, address);
    }
}
