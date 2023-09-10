package com.prgrms.nabmart.domain.item.service.request;

import com.prgrms.nabmart.domain.category.exception.NotFoundCategoryException;
import com.prgrms.nabmart.domain.item.ItemSortType;
import org.springframework.data.domain.PageRequest;

public record FindItemsBySubCategoryCommand(
    Long lastIdx,
    String mainCategoryName,
    String subCategoryName,
    PageRequest pageRequest,
    ItemSortType itemSortType) {

    private static final int DEFAULT_PAGE_NUMBER = 0;

    public static FindItemsBySubCategoryCommand of(
        Long lastIdx, String mainCategoryName, String subCategoryName, int pageSize, String sortType
    ) {

        validateCategoryName(mainCategoryName);
        validateCategoryName(subCategoryName);
        ItemSortType itemSortType = ItemSortType.from(sortType);
        PageRequest pageRequest = PageRequest.of(DEFAULT_PAGE_NUMBER, pageSize);
        if (isFirstItemId(lastIdx)) {
            lastIdx = redeclareLastIdx(itemSortType);
        }
        return new FindItemsBySubCategoryCommand(lastIdx, mainCategoryName, subCategoryName,
            pageRequest,
            itemSortType);
    }

    private static void validateCategoryName(String mainCategoryName) {
        if (mainCategoryName == null || mainCategoryName.isBlank()) {
            throw new NotFoundCategoryException("카테고리명은 필수 항목입니다.");
        }
    }

    private static boolean isFirstItemId(Long previousItemId) {
        return previousItemId < 0;
    }

    private static long redeclareLastIdx(ItemSortType itemSortType) {
        return itemSortType.getDefaultValue();
    }
}
