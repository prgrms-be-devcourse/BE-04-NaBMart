package com.prgrms.nabmart.domain.item.repository;

import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.ItemSortType;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    List<Item> findNewItemsOrderBy(Long lastIdx, Long lastItemId, ItemSortType sortType,
        Pageable pageable);

    List<Item> findHotItemsOrderBy(Long lastIdx, Long lastItemId, ItemSortType sortType,
        Pageable pageable);
}
