package com.prgrms.nabmart.domain.item.repository;

import com.prgrms.nabmart.domain.item.domain.Item;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // 신상템 - 가격 낮은 순
    Page<Item> findByCreatedAtAfterOrderByPriceAsc(LocalDateTime createdAt, Pageable pageable);

    // 신상템 - 가격 높은 순
    Page<Item> findByCreatedAtAfterOrderByPriceDesc(LocalDateTime createdAt, Pageable pageable);

    // 신상템 - 최근 등록 순
    Page<Item> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime createdAt, Pageable pageable);

    // 신상템 - 할인율 높은 순
    Page<Item> findByCreatedAtAfterOrderByDiscountDesc(LocalDateTime createdAt, Pageable pageable);

    // 신상템 - 인기도 순
    @Query("select i from Item i left join Review r on i = r.item left join LikeItem li on i = li.item where i.createdAt > :createdAt group by i order by count(li) * 0.3 + count(r) * 0.3 + avg(r.rate) * 0.4 desc")
    Page<Item> findNewItemsOrderByPopularity(@Param("createdAt") LocalDateTime createdAt, Pageable pageable);

    // 신상템 - 기본 순
    Page<Item> findByCreatedAtAfter(LocalDateTime createdAt, Pageable pageable);
}
