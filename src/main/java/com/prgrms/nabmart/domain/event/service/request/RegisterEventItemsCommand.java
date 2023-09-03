package com.prgrms.nabmart.domain.event.service.request;

import java.util.List;

public record RegisterEventItemsCommand(
    Long eventId,
    List<Long> items
) {

}
