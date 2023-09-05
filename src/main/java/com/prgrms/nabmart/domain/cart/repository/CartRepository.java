package com.prgrms.nabmart.domain.cart.repository;

import com.prgrms.nabmart.domain.cart.Cart;
import com.prgrms.nabmart.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(final User user);
}
