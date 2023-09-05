package com.prgrms.nabmart.domain.event.service;

import com.prgrms.nabmart.domain.event.domain.Event;
import com.prgrms.nabmart.domain.event.repository.EventRepository;
import com.prgrms.nabmart.domain.event.service.response.FindEventsResponse;
import com.prgrms.nabmart.domain.event.service.response.FindEventsResponse.FindEventResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional
    public Long registerEvent(RegisterEventCommand registerEventCommand) {
        Event event = new Event(registerEventCommand.title(), registerEventCommand.description());
        Event registered = eventRepository.save(event);
        return registered.getEventId();
    }

    @Transactional(readOnly = true)
    public FindEventsResponse findEvents() {
        List<Event> events = eventRepository.findAllByOrderByCreatedAtDesc();
        return FindEventsResponse.of(events.stream()
            .map(event -> new FindEventResponse(
                event.getEventId(),
                event.getTitle(),
                event.getDescription()
            )).collect(Collectors.toList()));
    }
}
