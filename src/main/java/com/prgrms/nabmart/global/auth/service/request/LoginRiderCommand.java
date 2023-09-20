package com.prgrms.nabmart.global.auth.service.request;

public record LoginRiderCommand(String username, String password) {

    public static LoginRiderCommand of(final String username, final String password) {
        return new LoginRiderCommand(username, password);
    }
}
