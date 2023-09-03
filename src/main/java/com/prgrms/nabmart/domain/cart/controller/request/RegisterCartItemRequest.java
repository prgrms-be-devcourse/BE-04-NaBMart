package com.prgrms.nabmart.domain.cart.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegisterCartItemRequest(

    @NotNull(message = "상품 아이디는 필수 입력 항목입니다.") @Positive(message = "상품 아이디는 양수입니다.") Long itemId,
    @NotNull(message = "수량은 필수 입력 항목입니다.") @Positive(message = "수량은 양수입니다.") Integer quantity) {

    public static RegisterCartItemRequest of(final Long itemId, final Integer quantity) {
        return new RegisterCartItemRequest(itemId, quantity);
    }
}
