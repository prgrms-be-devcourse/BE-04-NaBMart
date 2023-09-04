package com.prgrms.nabmart.domain.cart.service.response;

public record FindCartItemResponse(
    Long cartId,
    Long itemId,
    int quantity
) {

}
