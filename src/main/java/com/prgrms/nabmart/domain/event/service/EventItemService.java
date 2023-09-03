package com.prgrms.nabmart.domain.event.service;

import com.prgrms.nabmart.domain.event.domain.Event;
import com.prgrms.nabmart.domain.event.domain.EventItem;
import com.prgrms.nabmart.domain.event.exception.DuplicateEventItemException;
import com.prgrms.nabmart.domain.event.exception.NotExistsEventException;
import com.prgrms.nabmart.domain.event.repository.EventItemRepository;
import com.prgrms.nabmart.domain.event.repository.EventRepository;
import com.prgrms.nabmart.domain.event.service.request.RegisterEventItemsCommand;
import com.prgrms.nabmart.domain.item.domain.Item;
import com.prgrms.nabmart.domain.item.exception.NotExistsItemException;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventItemService {

    private final EventRepository eventRepository;
    private final EventItemRepository eventItemRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long registerEventItems(RegisterEventItemsCommand registerEventItemsCommand) {
        Event event = eventRepository.findById(registerEventItemsCommand.eventId())
            .orElseThrow(() -> new NotExistsEventException("존재하지 않는 이벤트입니다."));

        List<EventItem> eventItems = new ArrayList<>();
        List<Long> duplicatedItemIdList = new ArrayList<>();

        registerEventItemsCommand.items().forEach(itemId -> {
            Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotExistsItemException("존재하지 않는 아이템입니다."));

            if (eventItemRepository.existsByEventAndItem(event, item)) { // item 중복
                duplicatedItemIdList.add(itemId);
            } else {
                eventItems.add(new EventItem(event, item));
            }
        });

        if (duplicatedItemIdList.size() != 0) {
            throw new DuplicateEventItemException(
                duplicatedItemIdList.toString() + " 해당 아이템은 이미 이벤트에 등록된 상태입니다.");
        }

        eventItemRepository.saveAll(eventItems);
        return event.getEventId();
    }
}
