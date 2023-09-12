package com.prgrms.nabmart.domain.payment.repository;

import com.prgrms.nabmart.domain.payment.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrder_OrderIdAndUser_UserId(Long orderId, Long userId);
}
