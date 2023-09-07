package com.prgrms.nabmart.domain.item.repository;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.item.Item;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.itemId in ?1")
    List<Item> findByItemIdIn(Collection<Long> itemIds);

    List<Item> findByItemIdLessThanAndMainCategoryOrderByItemIdDesc(Long itemId,
        MainCategory mainCategory, Pageable pageable);

    // 신상품 - 주문 많은 순
    @Query("SELECT i FROM Item i WHERE i.createdAt > :createdAt AND SIZE(i.reviews) < :reviews ORDER BY SIZE(i.reviews) DESC, i.itemId DESC")
    List<Item> findNewItemOrderByOrders(@Param("createdAt") LocalDateTime createdAt, @Param("reviews") int reviews, Pageable pageable);

    // 신상품 - 가격 높은 순
    List<Item> findByCreatedAtAfterAndPriceLessThanOrderByPriceDescItemIdDesc(
        LocalDateTime createdAt, int price, Pageable pageable);

    // 신상품 - 가격 낮은 순
    List<Item> findByCreatedAtAfterAndPriceGreaterThanOrderByPriceAscItemIdDesc(
        LocalDateTime createdAt, int price, Pageable pageable);

    // 신상품 - 최근 등록 순
    List<Item> findByCreatedAtAfterAndItemIdLessThanOrderByCreatedAtDesc(LocalDateTime createdAt,
        Long itemId, Pageable pageable);

    // 신상품 - 할인순
    List<Item> findByCreatedAtAfterAndDiscountLessThanOrderByDiscountDescItemIdDesc(
        LocalDateTime createdAt, int discount, Pageable pageable);
}
