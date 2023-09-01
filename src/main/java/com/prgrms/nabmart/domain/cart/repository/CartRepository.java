package com.prgrms.nabmart.domain.cart.repository;

import com.prgrms.nabmart.domain.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
