package com.prgrms.nabmart.domain.event.repository;

import com.prgrms.nabmart.domain.event.domain.Event;
import com.prgrms.nabmart.domain.event.domain.EventItem;
import com.prgrms.nabmart.domain.item.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventItemRepository extends JpaRepository<EventItem, Long> {

    boolean existsByEventAndItem(Event event, Item item);

    @Query("SELECT ei.item FROM EventItem ei WHERE ei.event = :event AND ei.item IN :items")
    List<Item> findDuplicatedItems(
        @Param("event") Event event,
        @Param("items") List<Item> items
    );
}
