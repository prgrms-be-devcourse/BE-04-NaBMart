package com.prgrms.nabmart.domain.item.service.request;

import org.springframework.data.domain.Pageable;

public record FindLikeItemsCommand(Long userId, Pageable pageable) {

    public static FindLikeItemsCommand of(final Long userId, final Pageable pageable) {
        return new FindLikeItemsCommand(userId, pageable);
    }
}
