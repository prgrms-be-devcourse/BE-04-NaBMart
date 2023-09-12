package com.prgrms.nabmart.global.auth.service.request;

public record RiderLoginCommand(String username, String password) {

    public static RiderLoginCommand of(final String username, final String password) {
        return new RiderLoginCommand(username, password);
    }
}
