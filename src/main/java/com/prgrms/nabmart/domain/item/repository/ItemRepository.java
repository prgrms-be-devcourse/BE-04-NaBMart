package com.prgrms.nabmart.domain.item.repository;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
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

    //== 대카테고리 품목 조회 ==//
    // 대카테고리 전체 조회 - 최신 등록 순
    List<Item> findByItemIdLessThanAndMainCategoryOrderByItemIdDesc(Long itemId,
        MainCategory mainCategory, Pageable pageable);

    // 대카테고리 전체 조회 - 할인율 높은 순
    @Query("SELECT i "
        + "FROM Item i "
        + "WHERE i.mainCategory = :mainCategory "
        + "AND (i.discount < :discount OR (i.discount = :discount AND i.itemId < :itemId)) "
        + "ORDER BY i.discount DESC, i.itemId DESC")
    List<Item> findByMainCategoryAndDiscountDesc(
        @Param("itemId") Long itemId,
        @Param("discount") int discount,
        @Param("mainCategory") MainCategory mainCategory,
        Pageable pageable);

    // 대카테고리 전체 조회 - 금액 높은 순
    @Query("SELECT i "
        + "FROM Item i "
        + "WHERE i.mainCategory = :mainCategory "
        + "AND (i.price < :price OR (i.price = :price AND i.itemId < :itemId)) "
        + "ORDER BY i.price DESC, i.itemId DESC")
    List<Item> findByMainCategoryAndPriceDesc(
        @Param("itemId") Long itemId,
        @Param("price") int price,
        @Param("mainCategory") MainCategory mainCategory,
        Pageable pageable);

    // 대카테고리 전체 조회 - 금액 낮은 순
    @Query("SELECT i "
        + "FROM Item i "
        + "WHERE i.mainCategory = :mainCategory "
        + "AND (i.price > :price OR (i.price = :price AND i.itemId < :itemId)) "
        + "ORDER BY i.price ASC, i.itemId DESC")
    List<Item> findByByMainCategoryAndPriceAsc(
        @Param("itemId") Long itemId,
        @Param("price") int price,
        @Param("mainCategory") MainCategory mainCategory,
        Pageable pageable);

    // 대카테고리 전체 조회 - 주문 많은 순
    @Query("SELECT i "
        + "FROM Item i "
        + "LEFT JOIN OrderItem oi ON oi.item = i "
        + "WHERE i.mainCategory = :mainCategory "
        + "AND i.itemId < :itemId "
        + "GROUP BY i "
        + "HAVING (SUM(oi.quantity) <= :totalOrderedQuantity) "
        + "ORDER BY SUM(oi.quantity) DESC, i.itemId DESC")
    List<Item> findByOrderedQuantityAndMainCategory(
        @Param("itemId") Long itemId,
        @Param("totalOrderedQuantity") Long totalOrderedQuantity,
        @Param("mainCategory") MainCategory mainCategory,
        Pageable pageable);

    //== 소카테고리 품목 조회 ==//
    // 소카테고리 전체 조회 - 최신 등록 순
    List<Item> findByItemIdLessThanAndMainCategoryAndSubCategoryOrderByItemIdDesc(Long itemId,
        MainCategory mainCategory, SubCategory subCategory, Pageable pageable);

    // 소카테고리 전체 조회 - 할인율 높은 순
    @Query("SELECT i "
        + "FROM Item i "
        + "WHERE i.mainCategory = :mainCategory "
        + "AND i.subCategory = :subCategory "
        + "AND (i.discount < :discount OR (i.discount = :discount AND i.itemId < :itemId)) "
        + "ORDER BY i.discount DESC, i.itemId DESC")
    List<Item> findBySubCategoryAndDiscountDesc(
        @Param("itemId") Long itemId,
        @Param("discount") int discount,
        @Param("mainCategory") MainCategory mainCategory,
        @Param("subCategory") SubCategory subCategory,
        Pageable pageable);

    // 소카테고리 전체 조회 - 금액 높은 순
    @Query("SELECT i "
        + "FROM Item i "
        + "WHERE i.mainCategory = :mainCategory "
        + "AND i.subCategory = :subCategory "
        + "AND (i.price < :price OR (i.price = :price AND i.itemId < :itemId)) "
        + "ORDER BY i.price DESC, i.itemId DESC")
    List<Item> findBySubCategoryAndPriceDesc(
        @Param("itemId") Long itemId,
        @Param("price") int price,
        @Param("mainCategory") MainCategory mainCategory,
        @Param("subCategory") SubCategory subCategory,
        Pageable pageable);

    // 소카테고리 전체 조회 - 금액 낮은 순
    @Query("SELECT i "
        + "FROM Item i "
        + "WHERE i.mainCategory = :mainCategory "
        + "AND i.subCategory = :subCategory "
        + "AND (i.price > :price OR (i.price = :price AND i.itemId < :itemId)) "
        + "ORDER BY i.price ASC, i.itemId DESC")
    List<Item> findBySubCategoryAndPriceAsc(
        @Param("itemId") Long itemId,
        @Param("price") int price,
        @Param("mainCategory") MainCategory mainCategory,
        @Param("subCategory") SubCategory subCategory,
        Pageable pageable);

    // 소카테고리 전체 조회 - 주문 많은 순
    @Query("SELECT i "
        + "FROM Item i "
        + "LEFT JOIN OrderItem oi ON oi.item = i "
        + "WHERE i.mainCategory = :mainCategory "
        + "AND i.subCategory = :subCategory "
        + "AND i.itemId < :itemId "
        + "GROUP BY i "
        + "HAVING (SUM(oi.quantity) <= :totalOrderedQuantity) "
        + "ORDER BY SUM(oi.quantity) DESC, i.itemId DESC")
    List<Item> findByOrderedQuantityAndMainCategoryAndSubCategory(
        @Param("itemId") Long itemId,
        @Param("totalOrderedQuantity") Long totalOrderedQuantity,
        @Param("mainCategory") MainCategory mainCategory,
        @Param("subCategory") SubCategory subCategory,
        Pageable pageable);

    //== 신상품 조회 ==//
    // 신상품 - 주문 많은 순
    @Query("SELECT i "
        + "FROM Item i "
        + "LEFT JOIN OrderItem oi ON oi.item = i "
        + "WHERE i.createdAt > :createdAt "
        + "GROUP BY i "
        + "HAVING SUM(oi.quantity) < :totalOrderedQuantity OR SUM(oi.quantity) = NULL "
        + "ORDER BY SUM(oi.quantity) DESC, i.itemId DESC ")
    List<Item> findNewItemOrderByOrders(@Param("createdAt") LocalDateTime createdAt,
        @Param("totalOrderedQuantity") int totalOrderedQuantity, Pageable pageable);

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

    // 총 주문 수의 아이템 찾기
    @Query("SELECT i FROM Item i "
        + "LEFT JOIN OrderItem oi ON oi.item = i "
        + "GROUP BY i.itemId "
        + "HAVING SUM(oi.quantity) = :totalOrderedQuantity "
        + "ORDER BY i.itemId ASC")
    List<Item> findItemByTotalOrderedQuantity(
        @Param("totalOrderedQuantity") int totalOrderedQuantity);

    // 인기 상품 -> 주문 10회 이상 & 평점 3.8 이상
    // 인기 상품 - 가격 높은 순
    @Query("SELECT i FROM Item i "
        + "LEFT JOIN OrderItem oi ON oi.item = i "
        + "WHERE i.rate >= 3.8 "
        + "AND i.price < :price "
        + "GROUP BY i.itemId "
        + "HAVING SUM(oi.quantity) >= 10 "
        + "ORDER BY i.price DESC, i.itemId DESC ")
    List<Item> findHotItemOrderByPriceDesc(@Param("price") int price, Pageable pageable);

    // 인기 상품 - 가격 낮은 순
    @Query("SELECT i FROM Item i "
        + "LEFT JOIN OrderItem oi ON oi.item = i "
        + "WHERE i.rate >= 3.8 "
        + "AND i.price > :price "
        + "GROUP BY i.itemId "
        + "HAVING SUM(oi.quantity) >= 10 "
        + "ORDER BY i.price ASC, i.itemId DESC ")
    List<Item> findHotItemOrderByPriceAsc(@Param("price") int price, Pageable pageable);

    // 인기 상품 - 최근 등록 순
    @Query("SELECT i FROM Item i "
        + "LEFT JOIN OrderItem oi ON oi.item = i "
        + "WHERE i.rate >= 3.8 "
        + "AND i.itemId < :itemId "
        + "GROUP BY i.itemId "
        + "HAVING SUM(oi.quantity) >= 10 "
        + "ORDER BY i.itemId DESC ")
    List<Item> findHotItemOrderByItemIdDesc(@Param("itemId") Long itemId, Pageable pageable);

    // 인기 상품 - 할인순
    @Query("SELECT i FROM Item i "
        + "LEFT JOIN OrderItem oi ON oi.item = i "
        + "WHERE i.rate >= 3.8 "
        + "AND i.discount < :discount "
        + "GROUP BY i.itemId "
        + "HAVING SUM(oi.quantity) >= 10 "
        + "ORDER BY i.discount DESC, i.itemId DESC ")
    List<Item> findHotItemOrderByDiscountDesc(@Param("discount") int discount, Pageable pageable);

    // 인기 상품 - 주문 많은 순
    @Query("SELECT i "
        + "FROM Item i "
        + "LEFT JOIN OrderItem oi ON oi.item = i "
        + "WHERE i.rate >= 3.8 "
        + "GROUP BY i "
        + "HAVING SUM(oi.quantity) >= 10 AND "
        + "SUM(oi.quantity) < :totalOrders "
        + "ORDER BY SUM(oi.quantity) DESC, i.itemId DESC ")
    List<Item> findHotItemOrderByOrdersDesc(@Param("totalOrders") int totalOrders,
        Pageable pageable);
}
