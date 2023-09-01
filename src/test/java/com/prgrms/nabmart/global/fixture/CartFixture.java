package com.prgrms.nabmart.global.fixture;

import com.prgrms.nabmart.domain.cart.Cart;
import com.prgrms.nabmart.domain.user.User;

public final class CartFixture {

    private CartFixture() {

    }

    public static Cart cart(User user) {
        return new Cart(user);
    }
}
