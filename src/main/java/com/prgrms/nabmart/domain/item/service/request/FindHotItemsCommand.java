package com.prgrms.nabmart.domain.item.service.request;

import com.prgrms.nabmart.domain.item.ItemSortType;
import java.util.Objects;
import org.springframework.data.domain.PageRequest;

public record FindHotItemsCommand(
    Long lastIdx,
    Long lastItemId,
    PageRequest pageRequest,
    ItemSortType sortType
) {

    private static final int DEFAULT_PAGE_NUMBER = 0;

    public static FindHotItemsCommand of(Long lastIdx, Long lastItemId, int pageSize,
        String sortType) {
        if (isFirstIdx(lastIdx)) {
            lastIdx = Long.parseLong(String.valueOf(Integer.MAX_VALUE));
            if (Objects.equals(sortType, "LOWEST_AMOUNT") || Objects.equals(sortType, "NEW")) {
                lastIdx = Long.MIN_VALUE;
            }
        }

        ItemSortType itemSortType = ItemSortType.from(sortType);
        PageRequest pageRequest = PageRequest.of(DEFAULT_PAGE_NUMBER, pageSize);
        return new FindHotItemsCommand(lastIdx, lastItemId, pageRequest, itemSortType);
    }

    private static boolean isFirstIdx(Long previousItemId) {
        return previousItemId < 0;
    }
}
