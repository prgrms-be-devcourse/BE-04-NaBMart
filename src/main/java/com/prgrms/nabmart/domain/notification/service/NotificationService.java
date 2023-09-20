package com.prgrms.nabmart.domain.notification.service;

import static java.text.MessageFormat.format;

import com.prgrms.nabmart.domain.notification.repository.EmitterRepository;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 120;

    private final EmitterRepository emitterRepository;

    public SseEmitter connection(Long userId, String lastEventId) {
        String emitterId = userId + "_" + System.currentTimeMillis();
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(emitterId, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        emitter.onError(e -> emitterRepository.deleteById(emitterId));

        // 연결 직후 데이터 전송이 없으면 503 에러 발생. 에러 방지용 더미 데이터 전송
        sendNotification(emitter, emitterId, format("[Connected] UserId={0}", userId));

        // 클라이언트 미수신한 event를 모두 전송
        if(!lastEventId.isEmpty()) {
            Map<String, SseEmitter> events = emitterRepository.findAllByIdStartWith(userId);
            events.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    private void sendNotification(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(emitter.event()
                .id(emitterId)
                .data(data));
        } catch (IOException ex) {
            emitterRepository.deleteById(emitterId);
        }
    }
}
