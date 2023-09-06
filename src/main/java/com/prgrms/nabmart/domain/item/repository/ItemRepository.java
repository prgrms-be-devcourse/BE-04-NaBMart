package com.prgrms.nabmart.domain.item.repository;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.item.Item;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.itemId in ?1")
    List<Item> findByItemIdIn(Collection<Long> itemIds);

    List<Item> findByItemIdLessThanAndMainCategoryOrderByItemIdDesc(Long itemId,
        MainCategory mainCategory, Pageable pageable);
}
