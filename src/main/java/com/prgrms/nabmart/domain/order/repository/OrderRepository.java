package com.prgrms.nabmart.domain.order.repository;

import com.prgrms.nabmart.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
