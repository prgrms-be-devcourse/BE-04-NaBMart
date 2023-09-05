package com.prgrms.nabmart.domain.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("pending"),
    DELIVERING("delivering"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }
}
