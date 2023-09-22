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
        @DisplayName("예외: 제목 길이가 30자를 초과")
        void throwExceptionWhenTitleLengthGGraterThan31() {
            //given
            String titleGT30 = "a".repeat(31);

            //when
            //then
            assertThatThrownBy(() -> Notification.builder()
                .title(titleGT30)
                .content("내용")
                .userId(1L)
                .notificationType(NotificationType.DELIVERY)
                .build())
                .isInstanceOf(InvalidNotificationException.class);
        }

        @Test
        @DisplayName("예외: 내용 길이가 100자를 초과")
        void throwExceptionWhenContentLengthGraterThan100() {
            //given
            String contentGT100 = "a".repeat(101);

            //when
            //then
            assertThatThrownBy(() -> Notification.builder()
                .title("title")
                .content(contentGT100)
                .userId(1L)
                .notificationType(NotificationType.DELIVERY)
                .build())
                .isInstanceOf(InvalidNotificationException.class);
        }
    }
}
