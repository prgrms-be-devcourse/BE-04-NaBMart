package com.prgrms.nabmart.domain.order.service.response;

import com.prgrms.nabmart.domain.order.Order;
import java.time.LocalDateTime;
import java.util.List;

public record FindOrderDetailResponse(
    Long orderId,
    LocalDateTime createdAt,
    Integer total_price,
    String status,
    List<FindOrderDetailItemResponse> orderItems
) {

    public static FindOrderDetailResponse of(Order order,
        List<FindOrderDetailItemResponse> orderItems) {
        return new FindOrderDetailResponse(
            order.getOrderId(),
            order.getCreatedAt(),
            order.getPrice(),
            order.getStatus().toString(),
            orderItems
        );
    }
}
