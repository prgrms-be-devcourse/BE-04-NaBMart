package com.prgrms.nabmart.domain.event.service.response;

import java.util.List;

public record FindEventDetailResponse(
    EventDetailResponse event,
    List<EventItemResponse> items

) {

    public static FindEventDetailResponse of(final EventDetailResponse event,
        final List<EventItemResponse> items) {
        return new FindEventDetailResponse(event, items);
    }

    public record EventDetailResponse(Long eventId, String eventTitle, String eventDescription
    ) {

    }

    public record EventItemResponse(Long itemId, String name, int price, int discount,
                                    int reviewCount, int like, double rate) {

    }
}


