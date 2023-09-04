package com.prgrms.nabmart.global.fixture;

import com.prgrms.nabmart.domain.cart.Cart;
import com.prgrms.nabmart.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CartFixture {

    public static Cart cart(User user) {
        return new Cart(user);
    }
}
