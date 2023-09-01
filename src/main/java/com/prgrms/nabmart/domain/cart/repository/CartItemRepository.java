package com.prgrms.nabmart.domain.cart.repository;

import com.prgrms.nabmart.domain.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
