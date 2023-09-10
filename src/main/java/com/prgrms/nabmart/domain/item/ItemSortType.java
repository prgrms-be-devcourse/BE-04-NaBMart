package com.prgrms.nabmart.domain.item;

import com.prgrms.nabmart.domain.item.exception.NotFoundItemSortTypeException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemSortType {
    NEW(Long.MAX_VALUE),
    HIGHEST_AMOUNT(Integer.MAX_VALUE),
    LOWEST_AMOUNT(0),
    DISCOUNT(Integer.MAX_VALUE),
    POPULAR(Long.MAX_VALUE);

    private final long defaultValue;

    public static ItemSortType from(String sortType) {
        return Arrays.stream(ItemSortType.values())
            .filter(itemSortType -> itemSortType.name().equalsIgnoreCase(sortType))
            .findAny()
            .orElseThrow(() -> new NotFoundItemSortTypeException("요청하신 정렬기준은 존재하지 않습니다."));
    }
}
