package com.prgrms.nabmart.domain.order.service.response;

import com.prgrms.nabmart.domain.order.Order;
import java.util.List;
import java.util.stream.Collectors;

public record FindOrdersResponse(
    List<FindOrderResponse> orders,
    Integer totalPage
) {

    public static FindOrdersResponse of(List<Order> orders, Integer totalPage) {
        return new FindOrdersResponse(
            orders.stream()
                .map(FindOrderResponse::from)
                .collect(Collectors.toList()),
            totalPage
        );
    }
}
