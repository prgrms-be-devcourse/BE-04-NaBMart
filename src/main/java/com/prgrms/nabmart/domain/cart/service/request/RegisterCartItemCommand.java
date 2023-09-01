package com.prgrms.nabmart.domain.cart.service.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public record RegisterCartItemCommand(
    @NotNull(message = "장바구니 아이디는 필수 입력 항목입니다.") @Positive(message = "장바구니 아이디는 양수입니다.") Long cartId,
    @NotNull(message = "상품 아이디는 필수 입력 항목입니다.") @Positive(message = "상품 아이디는 양수입니다.") Long itemId,
    @NotNull(message = "수량은 필수 입력 항목입니다.") @Positive(message = "수량은 양수입니다.") Integer quantity) {

    public static RegisterCartItemCommand of(Long cartId, Long itemId, Integer quantity) {
        return new RegisterCartItemCommand(cartId, itemId, quantity);
    }
}
