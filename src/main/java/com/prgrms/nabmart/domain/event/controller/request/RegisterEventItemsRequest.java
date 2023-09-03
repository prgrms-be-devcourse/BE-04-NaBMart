package com.prgrms.nabmart.domain.event.controller.request;

import java.util.List;

public record RegisterEventItemsRequest(
    List<Long> items
) {

}
