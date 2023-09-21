package com.prgrms.nabmart.domain.notification.controller.request;

public record ConnectNotificationCommand(Long userId, String lastEventId) {

    public static ConnectNotificationCommand of(final Long userId, final String lastEventId) {
        return new ConnectNotificationCommand(userId, lastEventId);
    }
}
