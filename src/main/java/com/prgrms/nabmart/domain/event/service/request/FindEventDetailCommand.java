package com.prgrms.nabmart.domain.event.service.request;

public record FindEventDetailCommand(Long eventId) {

    public static FindEventDetailCommand from(final Long eventId) {
        return new FindEventDetailCommand(eventId);
    }
}
