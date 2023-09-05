package com.prgrms.nabmart.domain.event.service.response;

import java.util.List;

public record FindEventsResponse(List<FindEventResponse> events) {

    public static FindEventsResponse of(final List<FindEventResponse> findEventResponses) {
        return new FindEventsResponse(findEventResponses);
    }

    public record FindEventResponse(Long eventId, String name, String description) {

    }
}
