package com.prgrms.nabmart.domain.item.service.request;

import com.prgrms.nabmart.domain.category.exception.NotFoundCategoryException;
import com.prgrms.nabmart.domain.item.ItemSortType;
import org.springframework.data.domain.PageRequest;

public record FindItemsByMainCategoryCommand(
    Long previousItemId,
    String mainCategoryName,
    PageRequest pageRequest,
    ItemSortType itemSortType) {

    private static final int DEFAULT_PAGE_NUMBER = 0;

    public static FindItemsByMainCategoryCommand of(Long previousItemId, String mainCategoryName,
        int pageSize, String sortType) {

        validateMainCategoryName(mainCategoryName);
        if (isFirstItemId(previousItemId)) {
            previousItemId = Long.MAX_VALUE;
        }
        ItemSortType itemSortType = ItemSortType.from(sortType);
        PageRequest pageRequest = PageRequest.of(DEFAULT_PAGE_NUMBER, pageSize);
        return new FindItemsByMainCategoryCommand(previousItemId, mainCategoryName, pageRequest,
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
}
