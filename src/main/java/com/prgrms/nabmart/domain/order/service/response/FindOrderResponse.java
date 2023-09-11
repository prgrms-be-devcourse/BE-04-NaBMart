package com.prgrms.nabmart.domain.order.service.response;

import com.prgrms.nabmart.domain.order.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record FindOrderResponse(
    Long orderId,
    String name,
    String status,
    Integer totalPrice,
    LocalDateTime createdAt,
    List<FindOrdersItemResponse> items
) {

    public static FindOrderResponse from(Order order) {
        List<FindOrdersItemResponse> items = order.getOrderItems().stream()
            .map(FindOrdersItemResponse::from)
            .collect(Collectors.toList());

        return new FindOrderResponse(
            order.getOrderId(),
            order.getName(),
            order.getStatus().toString(),
            order.getPrice(),
            order.getCreatedAt(),
            items
        );
    }
}
