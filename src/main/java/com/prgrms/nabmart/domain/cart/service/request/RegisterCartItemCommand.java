package com.prgrms.nabmart.domain.cart.service.request;

public record RegisterCartItemCommand(
    Long cartId,
    Long itemId,
    Integer quantity) {

    public static RegisterCartItemCommand of(Long cartId, Long itemId, Integer quantity) {
        return new RegisterCartItemCommand(cartId, itemId, quantity);
    }
}
