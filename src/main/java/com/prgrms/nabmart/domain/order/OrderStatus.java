package com.prgrms.nabmart.domain.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("Created"),
    PROCESSING("Processing"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }
}
