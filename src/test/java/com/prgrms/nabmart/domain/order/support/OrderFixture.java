package com.prgrms.nabmart.domain.order.support;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderStatus;
import com.prgrms.nabmart.domain.user.User;

public class OrderFixture {

    private static final int PRICE = 10000;
    private static final String NAME = "orderName";

    public static Order getPendingOrder(long orderId, User user) {
        return Order.builder()
            .orderId(orderId)
            .name(NAME)
            .price(PRICE)
            .status(OrderStatus.PENDING)
            .user(user)
            .build();
    }

    public static Order getDeliveringOrder(long orderId, User user) {
        return Order.builder()
            .orderId(orderId)
            .name(NAME)
            .price(PRICE)
            .status(OrderStatus.DELIVERING)
            .user(user)
            .build();
    }

    public static Order getCompletedOrder(long orderId, User user) {
        return Order.builder()
            .orderId(orderId)
            .name(NAME)
            .price(PRICE)
            .status(OrderStatus.COMPLETED)
            .user(user)
            .build();
    }
}
