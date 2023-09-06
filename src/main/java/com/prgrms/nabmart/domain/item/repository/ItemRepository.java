package com.prgrms.nabmart.domain.item.repository;

import com.prgrms.nabmart.domain.item.domain.Item;
import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByCreatedAtAfterAndItemIdLessThanOrderByPriceAscItemIdDesc(
        LocalDateTime createdAt, Long itemId);

    // 신상템 - 가격 낮은 순
    List<Item> findByCreatedAtAfterOrderByPriceAscAndId(LocalDateTime createdAt, Pageable pageable);

    List<Item> findByCreatedAtAfterOrderByPriceAscItemIdAsc(LocalDateTime createdAt, Pageable pageable);

    // 신상템 - 가격 높은 순
    List<Item> findByCreatedAtAfterOrderByPriceDesc(LocalDateTime createdAt);

    // 신상템 - 최근 등록 순
    List<Item> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime createdAt);

    // 신상템 - 할인율 높은 순
    List<Item> findByCreatedAtAfterOrderByDiscountDesc(LocalDateTime createdAt);

    // 신상템 - 인기도 순
    @Query("select i from Item i left join Review r on i = r.item left join LikeItem li on i = li.item where i.createdAt > :createdAt group by i order by count(li) * 0.3 + count(r) * 0.3 + i.rate * 0.4 desc")
    List<Item> findNewItemsOrderByPopularity(@Param("createdAt") LocalDateTime createdAt);

    // 신상템 - 기본 순
    List<Item> findByCreatedAtAfter(LocalDateTime createdAt);
}
