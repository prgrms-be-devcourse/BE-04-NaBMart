package com.prgrms.nabmart.domain.order.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record CreateOrderRequest(
    @NotNull @Valid List<CreateOrderItemRequest> createOrderItemRequests) {

    public record CreateOrderItemRequest(
        @NotNull(message = "상품 아이디는 필수 입력 항목입니다")
        Long itemId,

        @Positive(message = "수량은 양수이어야 합니다")
        @NotNull(message = "수량은 필수 입력 항목 입니다.")
        Integer quantity
    ) {

    }
}
