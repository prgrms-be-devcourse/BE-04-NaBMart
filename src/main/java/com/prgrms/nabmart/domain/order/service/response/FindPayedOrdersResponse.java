package com.prgrms.nabmart.domain.order.service.response;

import com.prgrms.nabmart.domain.order.Order;
import java.util.List;

public record FindPayedOrdersResponse(
    List<FindPayedOrderResponse> orders,
    int page,
    long totalElements) {

    public static FindPayedOrdersResponse of(
        List<Order> orders,
        int page,
        long totalElements) {
        List<FindPayedOrderResponse> content = orders.stream()
            .map(FindPayedOrderResponse::from)
            .toList();
        return new FindPayedOrdersResponse(content, page, totalElements);
    }

    public record FindPayedOrderResponse(
        Long orderId,
        String name,
        int price) {

        public static FindPayedOrderResponse from(Order order) {
            return new FindPayedOrderResponse(
                order.getOrderId(),
                order.getName(),
                order.getPrice());
        }
    }
}
