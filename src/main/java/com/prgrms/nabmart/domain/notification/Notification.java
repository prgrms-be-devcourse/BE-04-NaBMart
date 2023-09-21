package com.prgrms.nabmart.domain.notification;

import com.prgrms.nabmart.domain.notification.exception.InvalidNotificationException;
import com.prgrms.nabmart.global.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    private static final int TITLE_LENGTH = 30;
    private static final int CONTENT_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(nullable = false)
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
