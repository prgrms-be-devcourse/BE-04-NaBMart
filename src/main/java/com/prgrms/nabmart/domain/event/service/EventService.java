package com.prgrms.nabmart.domain.event.service;

import com.prgrms.nabmart.domain.event.domain.Event;
import com.prgrms.nabmart.domain.event.domain.RegisterEventRequest;
import com.prgrms.nabmart.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional
    public Long registerEvent(Long userId, RegisterEventRequest registerEventRequest) {
        Event event = new Event(registerEventRequest.title(), registerEventRequest.description());
        Event registered = eventRepository.save(event);
        return registered.getEventId();
    }

}
