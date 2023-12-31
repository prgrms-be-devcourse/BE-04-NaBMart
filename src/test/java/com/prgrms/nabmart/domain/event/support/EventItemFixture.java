package com.prgrms.nabmart.domain.event.support;

import com.prgrms.nabmart.domain.event.domain.Event;
import com.prgrms.nabmart.domain.event.domain.EventItem;
import com.prgrms.nabmart.domain.item.Item;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventItemFixture {

    public static EventItem eventItem(Event event, Item item) {
        return new EventItem(event, item);
    }
}
