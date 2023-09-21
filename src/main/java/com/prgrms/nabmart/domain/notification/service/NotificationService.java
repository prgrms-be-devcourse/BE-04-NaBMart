package com.prgrms.nabmart.domain.notification.service;

import static java.text.MessageFormat.format;

import com.prgrms.nabmart.domain.notification.Notification;
import com.prgrms.nabmart.domain.notification.NotificationType;
import com.prgrms.nabmart.domain.notification.controller.request.ConnectNotificationCommand;
import com.prgrms.nabmart.domain.notification.repository.EmitterRepository;
import com.prgrms.nabmart.domain.notification.repository.NotificationRepository;
import com.prgrms.nabmart.domain.notification.service.request.SendNotificationCommand;
import com.prgrms.nabmart.domain.notification.service.response.NotificationResponse;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 120;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public SseEmitter connectNotification(ConnectNotificationCommand connectNotificationCommand) {
        Long userId = connectNotificationCommand.userId();
        String lastEventId = connectNotificationCommand.lastEventId();

        String emitterId = format("{0}_{1}",userId, System.currentTimeMillis());
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(emitterId, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        emitter.onError(e -> emitterRepository.deleteById(emitterId));

        // 연결 직후 데이터 전송이 없으면 503 에러 발생. 에러 방지용 더미 데이터 전송
        send(emitter, emitterId, format("[Connected] UserId={0}", userId));

        // 클라이언트 미수신한 event를 모두 전송
        if (!connectNotificationCommand.lastEventId().isEmpty()) {
            Map<String, SseEmitter> events = emitterRepository.findAllByIdStartWith(userId);
            events.entrySet().stream().filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> send(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    private void send(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                .id(emitterId)
                .data(data));
        } catch (IOException ex) {
            emitterRepository.deleteById(emitterId);
            log.error("알림 전송에 실패했습니다.", ex);
        }
    }

    @Transactional
    public void sendNotification(SendNotificationCommand sendNotificationCommand) {
        Long userId = sendNotificationCommand.userId();
        String title = sendNotificationCommand.title();
        String content = sendNotificationCommand.content();
        NotificationType notificationType = sendNotificationCommand.notificationType();

        verifyExistsUser(userId);
        Notification notification = Notification.builder()
            .title(title)
            .content(content)
            .userId(userId)
            .notificationType(notificationType)
            .build();
        notificationRepository.save(notification);

        Map<String, SseEmitter> emitters = emitterRepository.findAllByIdStartWith(userId);
        emitters.forEach((key, emitter) -> {
            send(emitter, key, NotificationResponse.from(notification));
        });
    }

    private void verifyExistsUser(Long userId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundUserException("존재하지 않는 유저입니다."));
    }

    private void send(SseEmitter emitter, String emitterId, NotificationResponse data) {
        try {
            emitter.send(SseEmitter.event()
                .id(emitterId)
                .name(data.notificationType().getValue())
                .data(data));
        } catch (IOException ex) {
            emitterRepository.deleteById(emitterId);
        }
    }
}
