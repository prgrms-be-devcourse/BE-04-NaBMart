package com.prgrms.nabmart.domain.event.support;

import com.prgrms.nabmart.domain.event.domain.Event;

public final class EventFixture {

    private static final String TITLE = "이벤트 이름";
    private static final String DESCRIPTION = "이벤트 설명";

    public static Event event() {
        return new Event(TITLE, DESCRIPTION);
    }
}
