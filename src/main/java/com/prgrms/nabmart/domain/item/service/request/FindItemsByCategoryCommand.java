package com.prgrms.nabmart.domain.item.service.request;

import com.prgrms.nabmart.domain.category.exception.NotFoundCategoryException;
import com.prgrms.nabmart.domain.item.ItemSortType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

@Slf4j
public record FindItemsByCategoryCommand(
    Long lastItemId,
    Long lastIdx,
    String mainCategoryName,
    String subCategoryName,
    PageRequest pageRequest,
    ItemSortType itemSortType) {

    private static final int DEFAULT_PAGE_NUMBER = 0;

    public static FindItemsByCategoryCommand of(
        Long lastItemId, Long lastIdx, String mainCategoryName, String subCategoryName,
        int pageSize,
        String sortType
    ) {
        validateMainCategoryName(mainCategoryName);
        ItemSortType itemSortType = ItemSortType.from(sortType);
        PageRequest pageRequest = PageRequest.of(DEFAULT_PAGE_NUMBER, pageSize);
        if (isFirstItemId(lastItemId)) {
            lastIdx = redeclareLastIdx(itemSortType);
            lastItemId = Long.MAX_VALUE;
        }
        return new FindItemsByCategoryCommand(lastItemId, lastIdx, mainCategoryName,
            subCategoryName,
            pageRequest,
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
        return itemSortType.getDefaultValue();
    }
}
