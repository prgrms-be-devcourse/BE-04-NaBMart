package com.prgrms.nabmart.domain.item.service.request;

import com.prgrms.nabmart.domain.category.exception.NotFoundCategoryException;
import com.prgrms.nabmart.domain.item.ItemSortType;
import org.springframework.data.domain.PageRequest;

public record FindItemsByMainCategoryCommand(
    Long lastIdx,
    String mainCategoryName,
    PageRequest pageRequest,
    ItemSortType itemSortType) {

    private static final int DEFAULT_PAGE_NUMBER = 0;

    public static FindItemsByMainCategoryCommand of(
        Long lastIdx, String mainCategoryName, int pageSize, String sortType
    ) {

        validateMainCategoryName(mainCategoryName);
        ItemSortType itemSortType = ItemSortType.from(sortType);
        PageRequest pageRequest = PageRequest.of(DEFAULT_PAGE_NUMBER, pageSize);
        if (isFirstItemId(lastIdx)) {
            lastIdx = redeclareLastIdx(itemSortType);
        }
        return new FindItemsByMainCategoryCommand(lastIdx, mainCategoryName, pageRequest,
            itemSortType);
    }

    private static void validateMainCategoryName(String mainCategoryName) {
        if (mainCategoryName == null || mainCategoryName.isBlank()) {
            throw new NotFoundCategoryException("카테고리명은 필수 항목입니다.");
        }
    }

    private static boolean isFirstItemId(Long previousItemId) {
        return previousItemId < 0;
    }

    private static long redeclareLastIdx(ItemSortType itemSortType) {
        return itemSortType.isLowestSort() ? 0L : Long.MAX_VALUE;
    }
}
