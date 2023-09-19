package com.prgrms.nabmart.domain.item.repository;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.ItemSortType;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    List<Item> findNewItemsOrderBy(Long lastIdx, Long lastItemId, ItemSortType sortType,
        Pageable pageable);

    List<Item> findHotItemsOrderBy(Long lastIdx, Long lastItemId, ItemSortType sortType,
        Pageable pageable);

    List<Item> findByMainCategoryOrderBy(MainCategory mainCategory, Long lastIdx, Long lastItemId,
        ItemSortType sortType, Pageable pageable);

    List<Item> findBySubCategoryOrderBy(MainCategory mainCategory, SubCategory subCategory,
        Long lastIdx, Long lastItemId, ItemSortType sortType, Pageable pageable);
}
