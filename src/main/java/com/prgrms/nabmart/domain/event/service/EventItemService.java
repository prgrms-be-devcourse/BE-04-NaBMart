package com.prgrms.nabmart.domain.event.service;

import com.prgrms.nabmart.domain.event.domain.Event;
import com.prgrms.nabmart.domain.event.domain.EventItem;
import com.prgrms.nabmart.domain.event.exception.DuplicateEventItemException;
import com.prgrms.nabmart.domain.event.exception.NotFoundEventException;
import com.prgrms.nabmart.domain.event.repository.EventItemRepository;
import com.prgrms.nabmart.domain.event.repository.EventRepository;
import com.prgrms.nabmart.domain.event.service.request.RegisterEventItemsCommand;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
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
            .orElseThrow(() -> new NotFoundEventException("존재하지 않는 이벤트입니다."));

        List<Item> items = itemRepository.findByItemIdIn(registerEventItemsCommand.items());

        List<Item> duplicatedItems = eventItemRepository.findDuplicatedItems(event, items);

        if (!duplicatedItems.isEmpty()) {
            List<Long> duplicatedItemIdList = duplicatedItems.stream()
                .map(Item::getItemId).toList();
            throw new DuplicateEventItemException(
                duplicatedItemIdList.toString() + " 해당 아이템은 이미 이벤트에 등록된 상태입니다.");
        }

        List<EventItem> eventItems = items.stream()
            .map(item -> new EventItem(event, item))
            .toList();

        eventItemRepository.saveAll(eventItems);
        return event.getEventId();
    }
}
