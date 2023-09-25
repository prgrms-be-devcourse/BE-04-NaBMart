package com.prgrms.nabmart.domain.notification.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

import com.prgrms.nabmart.domain.notification.controller.request.ConnectNotificationCommand;
import com.prgrms.nabmart.domain.notification.repository.EmitterRepository;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    NotificationService notificationService;

    @Mock
    UserRepository userRepository;

    @Mock
    EmitterRepository emitterRepository;

    @Nested
    @DisplayName("connectNotification 메서드 실행 시")
    class ConnectNotificationTest {

        @Test
        @DisplayName("성공")
        void success() {
            //given
            long userId = 1L;
            ConnectNotificationCommand connectNotificationCommand
                = ConnectNotificationCommand.of(userId, "");

            //when
            SseEmitter emitter = notificationService.connectNotification(
                connectNotificationCommand);

            //then
            then(emitterRepository).should().save(any(), any());
        }
    }
}
