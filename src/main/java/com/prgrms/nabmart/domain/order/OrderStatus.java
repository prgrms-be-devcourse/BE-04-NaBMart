package com.prgrms.nabmart.domain.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("pending"),
    PAYING("paying"),
    PAYED("payed"),
    DELIVERING("delivering"),
    COMPLETED("completed"),
    CANCELED("canceled");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }
}
