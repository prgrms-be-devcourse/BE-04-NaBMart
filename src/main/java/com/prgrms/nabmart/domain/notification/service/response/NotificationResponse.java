package com.prgrms.nabmart.domain.notification.service.response;

import com.prgrms.nabmart.domain.notification.Notification;
import com.prgrms.nabmart.domain.notification.NotificationType;
import java.time.LocalDateTime;

public record NotificationResponse(
    Long notificationId,
    String content,
    NotificationType notificationType,
    Long userId,
    LocalDateTime createdAt) {

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
            notification.getNotificationId(),
            notification.getContent(),
            notification.getNotificationType(),
            notification.getUserId(),
            notification.getCreatedAt());
    }
}
