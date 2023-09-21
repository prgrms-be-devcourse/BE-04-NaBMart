package com.prgrms.nabmart.domain.notification;

import com.prgrms.nabmart.domain.notification.exception.InvalidNotificationException;
import com.prgrms.nabmart.global.BaseTimeEntity;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    private static final int TITLE_LENGTH = 30;
    private static final int CONTENT_LENGTH = 100;

    private String title;
    private String content;
    private NotificationType notificationType;
    private Long userId;

    @Builder
    public Notification(
        String title,
        String content,
        Long userId,
        NotificationType notificationType) {
        validateTitle(title);
        validateContent(content);
        this.title = title;
        this.content = content;
        this.notificationType = notificationType;
        this.userId = userId;
    }

    private void validateTitle(String title) {
        if (Objects.nonNull(title) && title.length() > TITLE_LENGTH) {
            throw new InvalidNotificationException("제목의 길이는 20자 이하여야 합니다.");
        }
    }

    private void validateContent(String content) {
        if (Objects.nonNull(content) && content.length() > CONTENT_LENGTH) {
            throw new InvalidNotificationException("내용의 길이는 50자 이하여야 합니다.");
        }
    }
}
