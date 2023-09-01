package com.prgrms.nabmart.domain.cart.service.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public record RegisterCartItemCommand(Long cartId, Long itemId, Integer quantity,
                                      Boolean isChecked) {

    public static RegisterCartItemCommand of(Long cartId, Long itemId, Integer quantity,
        Boolean isChecked) {
        return new RegisterCartItemCommand(cartId, itemId, quantity, isChecked);
    }
}
