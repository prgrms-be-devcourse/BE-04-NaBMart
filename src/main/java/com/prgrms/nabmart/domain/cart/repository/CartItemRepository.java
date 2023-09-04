package com.prgrms.nabmart.domain.cart.repository;

import com.prgrms.nabmart.domain.cart.CartItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByCartItemIdAndOrderByCreatedAt(final Long cartItemId);
}
