package com.prgrms.nabmart.domain.cart.service.request;

public record RegisterCartItemCommand(
    Long userId,
    Long itemId,
    Integer quantity) {

    public static RegisterCartItemCommand of(
        final Long userId,
        final Long itemId,
        final Integer quantity) {
        return new RegisterCartItemCommand(userId, itemId, quantity);
    }
}
