package com.prgrms.nabmart.global.auth.service.response;

public record RiderLoginResponse(String accessToken) {

    public static RiderLoginResponse from(String accessToken) {
        return new RiderLoginResponse(accessToken);
    }
}
