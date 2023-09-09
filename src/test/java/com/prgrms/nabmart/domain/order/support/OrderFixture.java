package com.prgrms.nabmart.domain.order.support;

import static com.prgrms.nabmart.domain.item.support.ItemFixture.item;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderItem;
import com.prgrms.nabmart.domain.order.OrderStatus;
import com.prgrms.nabmart.domain.order.service.response.FindOrderDetailResponse;
import com.prgrms.nabmart.domain.user.User;
import java.util.List;

public class OrderFixture {

    private static final int PRICE = 10000;
    private static final String NAME = "orderName";

    public static Order pendingOrder(long orderId, User user) {
        return Order.builder()
            .orderId(orderId)
            .name(NAME)
            .price(PRICE)
            .status(OrderStatus.PENDING)
            .orderItems(List.of(orderItem()))
            .user(user)
            .build();
    }

    public static Order deliveringOrder(long orderId, User user) {
        return Order.builder()
            .orderId(orderId)
            .name(NAME)
            .price(PRICE)
            .status(OrderStatus.DELIVERING)
            .orderItems(List.of(orderItem()))
            .user(user)
            .build();
    }

    public static Order completedOrder(long orderId, User user) {
        return Order.builder()
            .orderId(orderId)
            .name(NAME)
            .price(PRICE)
            .status(OrderStatus.COMPLETED)
            .orderItems(List.of(orderItem()))
            .user(user)
            .build();
    }

    private static OrderItem orderItem() {
        return OrderItem.builder()
            .quantity(1)
            .item(item())
            .build();
    }

    public static FindOrderDetailResponse orderDetailResponse(Order order) {
        return FindOrderDetailResponse.from(order);
    }
}
