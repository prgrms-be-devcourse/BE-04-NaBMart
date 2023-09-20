package com.prgrms.nabmart.domain.notification.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterMemoryRepository implements
    EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public void save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
    }

    @Override
    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }

    @Override
    public Map<String, SseEmitter> findAllByIdStartWith(Long userId) {
        String emitterIdPrefix = userId + "_";
        return emitters.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(emitterIdPrefix))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
