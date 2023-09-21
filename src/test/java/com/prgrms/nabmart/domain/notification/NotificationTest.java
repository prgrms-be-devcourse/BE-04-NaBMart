package com.prgrms.nabmart.domain.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.prgrms.nabmart.domain.notification.exception.InvalidNotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Nested
    @DisplayName("notification 생성 시")
    class NewNotificationTest {

        @Test
        @DisplayName("성공")
        void success() {
            //given
            String title = "제목";
            String content = "내용";

            //when
            Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .userId(1L)
                .notificationType(NotificationType.DELIVERY)
                .build();

            //then
            assertThat(notification.getTitle()).isEqualTo(title);
            assertThat(notification.getContent()).isEqualTo(content);
        }

        @Test
        @DisplayName("예외: 제목 길이가 20자를 초과")
        void throwExceptionWhenTitleLengthGGraterThan20() {
            //given
            String titleGT20 = "a".repeat(21);

            //when
            //then
            assertThatThrownBy(() -> Notification.builder()
                .title(titleGT20)
                .content("내용")
                .userId(1L)
                .notificationType(NotificationType.DELIVERY)
                .build())
                .isInstanceOf(InvalidNotificationException.class);
        }

        @Test
        @DisplayName("예외: 내용 길이가 50자를 초과")
        void throwExceptionWhenContentLengthGraterThan50() {
            //given
            String contentGT50 = "a".repeat(51);

            //when
            //then
            assertThatThrownBy(() -> Notification.builder()
                .title("title")
                .content(contentGT50)
                .userId(1L)
                .notificationType(NotificationType.DELIVERY)
                .build())
                .isInstanceOf(InvalidNotificationException.class);
        }
    }
}
