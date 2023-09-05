package com.prgrms.nabmart.domain.cart.service.response;

public record FindCartItemResponse(
    Long cartId,
    Long itemId,
    int quantity
) {

    public static FindCartItemResponse of(
        final Long cartId,
        final Long itemId,
        final int quantity
    ) {
        return new FindCartItemResponse(cartId,
            itemId,
            quantity);
    }
}
