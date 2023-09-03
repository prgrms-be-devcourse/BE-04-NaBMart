package com.prgrms.nabmart.domain.cart.repository;

import com.prgrms.nabmart.domain.cart.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findCartByUser(Long userId);
}
