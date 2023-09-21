package com.prgrms.nabmart.domain.notification.repository;

import java.util.Map;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {

    void save(String emitterId, SseEmitter sseEmitter);

    void deleteById(String emitterId);

    Map<String, SseEmitter> findAllByIdStartWith(Long userId);
}
