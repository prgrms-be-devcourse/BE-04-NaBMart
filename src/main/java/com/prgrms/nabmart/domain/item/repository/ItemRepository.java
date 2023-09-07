package com.prgrms.nabmart.domain.item.repository;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.item.Item;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.itemId in ?1")
    List<Item> findByItemIdIn(Collection<Long> itemIds);

    // 대카테고리 전체 조회 - 최신 등록 순
    List<Item> findByItemIdLessThanAndMainCategoryOrderByItemIdDesc(Long itemId,
        MainCategory mainCategory, Pageable pageable);

    // 대카테고리 전체 조회 - 할인율 높은 순
    List<Item> findByDiscountLessThanAndMainCategoryOrderByDiscountDescItemIdDesc(int discount,
        MainCategory mainCategory, Pageable pageable);

    // 대카테고리 전체 조회 - 금액 높은 순
    List<Item> findByPriceLessThanAndMainCategoryOrderByPriceDescItemIdDesc(int price,
        MainCategory mainCategory, Pageable pageable);

    // 대카테고리 전체 조회 - 금액 낮은 순
    List<Item> findByPriceGreaterThanAndMainCategoryOrderByPriceAscItemIdDesc(int price,
        MainCategory mainCategory, Pageable pageable);

    // 대카테고리 전체 조회 - 주문 많은 순
    @Query("SELECT i "
        + "FROM Item i "
        + "LEFT JOIN OrderItem oi ON oi.item = i "
        + "WHERE i.mainCategory = :mainCategory "
        + "GROUP BY i "
        + "HAVING SUM(oi.quantity) < :totalOrderedQuantity "
        + "ORDER BY SUM(oi.quantity) DESC")
    List<Item> findByOrderCountAndMainCategory(
        @Param("totalOrderedQuantity") int totalOrderedQuantity,
        @Param("mainCategory") MainCategory mainCategory,
        Pageable pageable);
}
