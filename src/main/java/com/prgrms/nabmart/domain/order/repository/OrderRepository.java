package com.prgrms.nabmart.domain.order.repository;

import com.prgrms.nabmart.domain.order.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderIdAndUser_UserId(Long orderId, Long userId);
}
