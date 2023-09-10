package com.prgrms.nabmart.domain.delivery.service.request;

public record RiderSignupCommand(String username, String password, String address) {

    public static RiderSignupCommand of(
        final String username,
        final String password,
        final String address) {
        return new RiderSignupCommand(username, password, address);
    }
}
