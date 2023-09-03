package com.prgrms.nabmart.domain.event.service;

import com.prgrms.nabmart.domain.event.domain.Event;
import com.prgrms.nabmart.domain.event.domain.EventItem;
import com.prgrms.nabmart.domain.event.exception.EventNotFoundException;
import com.prgrms.nabmart.domain.event.repository.EventItemRepository;
import com.prgrms.nabmart.domain.event.repository.EventRepository;
import com.prgrms.nabmart.domain.event.service.request.RegisterEventCommand;
import com.prgrms.nabmart.domain.event.service.request.RegisterEventItemsCommand;
import com.prgrms.nabmart.domain.item.exception.ItemNotFoundException;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventItemRepository eventItemRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long registerEvent(RegisterEventCommand registerEventCommand) {
        Event event = new Event(registerEventCommand.title(), registerEventCommand.description());
        Event registered = eventRepository.save(event);
        return registered.getEventId();
    }

    @Transactional
    public Long registerEventItems(RegisterEventItemsCommand registerEventItemsCommand) {
        Event event = eventRepository.findById(registerEventItemsCommand.eventId())
            .orElseThrow(() -> new EventNotFoundException("존재하지 않는 이벤트입니다."));

        List<EventItem> eventItems = new ArrayList<>();

        registerEventItemsCommand.items().forEach(id -> {
            EventItem eventItem = new EventItem(event, itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("존재하지 않는 아이템입니다.")));
            eventItems.add(eventItem);
        });

        eventItemRepository.saveAll(eventItems);

        return event.getEventId();
    }

}
