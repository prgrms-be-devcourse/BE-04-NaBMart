package com.prgrms.nabmart.domain.order.repository;

import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.order.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findByItem(Item item);

    @Query("SELECT SUM(oi.quantity) "
        + "FROM OrderItem oi "
        + "WHERE oi.item.id = :itemId")
    Long countByOrderItemId(@Param("itemId") Long itemId);
}
