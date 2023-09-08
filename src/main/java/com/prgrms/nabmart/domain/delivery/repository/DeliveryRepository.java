package com.prgrms.nabmart.domain.delivery.repository;

import com.prgrms.nabmart.domain.delivery.Delivery;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("select d from Delivery d join fetch d.order where d.order.orderId = :orderId")
    Optional<Delivery> findByOrderIdWithOrder(@Param("orderId") Long orderId);
}
