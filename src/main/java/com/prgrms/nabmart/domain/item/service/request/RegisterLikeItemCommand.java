package com.prgrms.nabmart.domain.item.service.request;

public record RegisterLikeItemCommand(Long userId, Long itemId) {

    public static RegisterLikeItemCommand of(final Long userId, final Long itemId) {
        return new RegisterLikeItemCommand(userId, itemId);
    }
}
