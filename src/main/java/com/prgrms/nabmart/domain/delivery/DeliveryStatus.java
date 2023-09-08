package com.prgrms.nabmart.domain.delivery;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DeliveryStatus {
    ACCEPTING_ORDER,
    START_DELIVERY,
    DELIVERED;
}
