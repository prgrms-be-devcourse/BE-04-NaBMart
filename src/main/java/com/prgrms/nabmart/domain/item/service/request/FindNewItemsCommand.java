package com.prgrms.nabmart.domain.item.service.request;

import com.prgrms.nabmart.domain.item.domain.ItemSortType;

public record FindNewItemsCommand(int lastItemId, int pageSize, ItemSortType sortType) {

    public static FindNewItemsCommand of(final int lastItemId, final int pageSize,
        final ItemSortType sortType) {
        return new FindNewItemsCommand(lastItemId, pageSize, sortType);
    }
}
