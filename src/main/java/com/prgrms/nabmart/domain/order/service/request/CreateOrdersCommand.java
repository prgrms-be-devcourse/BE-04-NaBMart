package com.prgrms.nabmart.domain.order.service.request;

import com.prgrms.nabmart.domain.order.controller.request.CreateOrderRequest;

public record CreateOrdersCommand(
    Long userId,
    CreateOrderRequest createOrderRequest
) {

    public static CreateOrdersCommand of(final Long userId,
        final CreateOrderRequest createOrderRequest) {
        return new CreateOrdersCommand(userId, createOrderRequest);
    }
}
