package com.prgrms.nabmart.domain.item.service.request;

import com.prgrms.nabmart.domain.item.domain.ItemSortType;

public record FindNewItemsCommand(int page, int pageSize, ItemSortType sortType) {

    public static FindNewItemsCommand of(final int page, final int pageSize,
        final ItemSortType sortType) {
        return new FindNewItemsCommand(page, pageSize, sortType);
    }
}
