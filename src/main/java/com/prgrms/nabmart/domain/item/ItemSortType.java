package com.prgrms.nabmart.domain.item;

import com.prgrms.nabmart.domain.item.exception.NotFoundItemSortTypeException;
import java.util.Arrays;

public enum ItemSortType {
    POPULAR,
    HIGHEST_AMOUNT,
    LOWEST_AMOUNT,
    NEW,
    DISCOUNT;

    public static ItemSortType from(String sortType) {
        return Arrays.stream(ItemSortType.values())
            .filter(itemSortType -> itemSortType.name().equalsIgnoreCase(sortType))
            .findAny()
            .orElseThrow(() -> new NotFoundItemSortTypeException("요청하신 정렬기준은 존재하지 않습니다."));
    }
}
