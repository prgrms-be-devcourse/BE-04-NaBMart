package com.prgrms.nabmart.domain.delivery.repository;

import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import com.prgrms.nabmart.domain.delivery.Rider;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.user.User;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("select d from Delivery d join fetch d.order where d.order.orderId = :orderId")
    Optional<Delivery> findByOrderIdWithOrder(@Param("orderId") Long orderId);

    @Query(value = "select d from Delivery d"
        + " where d.deliveryStatus"
        + " = com.prgrms.nabmart.domain.delivery.DeliveryStatus.ACCEPTING_ORDER"
        + " and d.rider is null")
    Page<Delivery> findWaitingDeliveries(Pageable pageable);

    @Query(value = "select d from Delivery d"
        + " where d.rider = :rider and d.deliveryStatus in :statuses")
    Page<Delivery> findRiderDeliveries(
        @Param("rider") Rider rider,
        @Param("statuses") List<DeliveryStatus> deliveryStatuses,
        Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select d from Delivery d where d.deliveryId = :deliveryId")
    Optional<Delivery> findByIdOptimistic(@Param("deliveryId") Long deliveryId);

    @Query("select d from Delivery d join d.order o where o.user = :user")
    List<Delivery> findAllByUser(@Param("user") User user);

    boolean existsByOrder(Order order);
}
