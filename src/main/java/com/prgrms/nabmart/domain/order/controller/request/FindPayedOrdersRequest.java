package com.prgrms.nabmart.domain.order.controller.request;

import jakarta.validation.constraints.PositiveOrZero;

public record FindPayedOrdersRequest(
    @PositiveOrZero(message = "페이지 번호는 음수일 수 없습니다.")
    Integer page) {

    public FindPayedOrdersRequest {
        page = 0;
    }
}
