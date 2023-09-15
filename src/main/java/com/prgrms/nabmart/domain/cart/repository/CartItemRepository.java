package com.prgrms.nabmart.domain.cart.repository;

import com.prgrms.nabmart.domain.cart.Cart;
import com.prgrms.nabmart.domain.cart.CartItem;
import com.prgrms.nabmart.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByCartItemIdOrderByCreatedAt(Long cartItemId);

    List<CartItem> findAllByCartOrderByCreatedAt(Cart cart);

    @Modifying
    @Query("delete from CartItem ci"
        + " where ci.cart ="
        + " (select c from Cart c where c.user = :user)")
    void deleteByUser(@Param("user") User user);
}
