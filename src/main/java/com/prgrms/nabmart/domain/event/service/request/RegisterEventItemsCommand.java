package com.prgrms.nabmart.domain.event.service.request;

import java.util.List;

public record RegisterEventItemsCommand(Long eventId, List<Long> items) {

    public static RegisterEventItemsCommand from(final Long eventId, final List<Long> items) {
        return new RegisterEventItemsCommand(eventId, items);
    }
}
