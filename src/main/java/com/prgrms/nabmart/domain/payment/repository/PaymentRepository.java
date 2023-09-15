package com.prgrms.nabmart.domain.payment.repository;

import com.prgrms.nabmart.domain.payment.Payment;
import com.prgrms.nabmart.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrder_UuidAndUser_UserId(String uuid, Long userId);

    void deleteByUser(User user);
}
