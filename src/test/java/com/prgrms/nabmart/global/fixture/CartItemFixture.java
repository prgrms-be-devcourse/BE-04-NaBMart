package com.prgrms.nabmart.global.fixture;

import com.prgrms.nabmart.domain.cart.Cart;
import com.prgrms.nabmart.domain.cart.CartItem;
import com.prgrms.nabmart.domain.item.domain.Item;

public final class CartItemFixture {

    private CartItemFixture() {
    }

    public static CartItem cartItem(Cart cart, Item item, int quantity) {
        return new CartItem(cart, item, quantity);
    }
}
