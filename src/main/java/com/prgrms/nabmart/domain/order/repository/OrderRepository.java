package com.prgrms.nabmart.domain.order.repository;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderStatus;
import com.prgrms.nabmart.domain.user.User;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderIdAndUser_UserId(Long orderId, Long userId);

    Page<Order> findByUser_UserId(Long userId, Pageable pageable);

    Optional<Order> findByUuidAndUser_UserId(String uuid, Long userId);

    @Query("SELECT o FROM Order o WHERE o.createdAt <= :expiredTime AND o.status IN :statusList")
    List<Order> findByStatusInBeforeExpiredTime(@Param("expiredTime") LocalDateTime expiredTime,
        @Param("statusList") List<OrderStatus> statusList);

    void deleteByUser(User findUser);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Order o where o.orderId = :orderId")
    Optional<Order> findByIdPessimistic(@Param("orderId") Long orderId);
}
