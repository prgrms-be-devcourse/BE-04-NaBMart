package com.prgrms.nabmart.domain.cart.support;

import com.prgrms.nabmart.domain.cart.Cart;
import com.prgrms.nabmart.domain.cart.CartItem;
import com.prgrms.nabmart.domain.item.Item;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CartItemFixture {

    public static CartItem cartItem(Cart cart, Item item, int quantity) {
        return new CartItem(cart, item, quantity);
    }
}
