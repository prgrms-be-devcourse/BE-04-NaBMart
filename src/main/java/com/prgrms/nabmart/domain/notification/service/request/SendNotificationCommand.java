package com.prgrms.nabmart.domain.notification.service.request;

import com.prgrms.nabmart.domain.notification.NotificationType;

public record SendNotificationCommand(
    Long userId,
    String title,
    String content,
    NotificationType notificationType) {

    public static SendNotificationCommand of(
        final Long userId,
        final String title,
        final String content,
        final NotificationType notificationType) {
        return new SendNotificationCommand(userId, title, content, notificationType);
    }
}
