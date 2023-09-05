package com.prgrms.nabmart.domain.cart.service.request;

public record UpdateCartItemCommand(Long cartId, Integer quantity) {

    public static UpdateCartItemCommand of(final Long cartId, final Integer quantity) {
        return new UpdateCartItemCommand(cartId, quantity);
    }
}
