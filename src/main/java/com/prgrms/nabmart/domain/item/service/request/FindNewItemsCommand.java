package com.prgrms.nabmart.domain.item.service.request;

import com.prgrms.nabmart.domain.item.ItemSortType;
import java.util.Objects;
import org.springframework.data.domain.PageRequest;

public record FindNewItemsCommand(
    Long lastIdx,
    PageRequest pageRequest,
    ItemSortType sortType) {

    private static final int DEFAULT_PAGE_NUMBER = 0;

    public static FindNewItemsCommand of(Long lastIdx, int pageSize, String sortType) {
        if (isFirstIdx(lastIdx)) {
            lastIdx = Long.parseLong(String.valueOf(Integer.MAX_VALUE));
            if (Objects.equals(sortType, "LOWEST_AMOUNT")) {
                lastIdx = Long.MIN_VALUE;
            }
        }

        ItemSortType itemSortType = ItemSortType.from(sortType);
        PageRequest pageRequest = PageRequest.of(DEFAULT_PAGE_NUMBER, pageSize);
        return new FindNewItemsCommand(lastIdx, pageRequest, itemSortType);
    }

    private static boolean isFirstIdx(Long previousItemId) {
        return previousItemId < 0;
    }
}
