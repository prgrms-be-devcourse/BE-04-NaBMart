package com.prgrms.nabmart.domain.item.service.request;

import org.springframework.data.domain.Pageable;

public record FindLikeItemsCommand(Long userId, Pageable pageable) {

    public static FindLikeItemsCommand of(Long userId, Pageable pageable) {
        return new FindLikeItemsCommand(userId, pageable);
    }
}
