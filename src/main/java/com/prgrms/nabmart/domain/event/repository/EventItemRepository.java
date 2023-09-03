package com.prgrms.nabmart.domain.event.repository;

import com.prgrms.nabmart.domain.event.domain.Event;
import com.prgrms.nabmart.domain.event.domain.EventItem;
import com.prgrms.nabmart.domain.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventItemRepository extends JpaRepository<EventItem, Long> {

    boolean existsByEventAndItem(Event event, Item item);

}
