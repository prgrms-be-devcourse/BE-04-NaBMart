package com.prgrms.nabmart.global.fixture;

import com.prgrms.nabmart.domain.event.domain.Event;

public final class EventFixture {

    private static final String EVENT_NAME = "이벤트";
    private static final String EVENT_DESCRIPTION = "이벤트 설명";

    private EventFixture() {
    }

    public static Event event() {
        return new Event(EVENT_NAME, EVENT_DESCRIPTION);
    }
}
