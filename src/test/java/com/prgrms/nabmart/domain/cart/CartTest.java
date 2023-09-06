package com.prgrms.nabmart.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import com.prgrms.nabmart.global.fixture.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CartTest {

    User givenUser;
    Cart givenCart;

    @BeforeEach
    void setUp() {
        givenUser = UserFixture.user();
    }

    @Nested
    @DisplayName("장바구니 생성 시")
    class NewCartTest {

        @Test
        @DisplayName("성공")
        void newCart() {
            // given

            // when
            givenCart = new Cart(givenUser);

            // then
            assertThat(givenCart.getUser()).isEqualTo(givenUser);
        }

        @Test
        @DisplayName("예외 : 사용자가 null")
        void throwExceptionWhenUserIsNull() {
            // given
            User nullUser = null;

            // when
            Exception exception = catchException(() -> new Cart(nullUser));

            // then
            assertThat(exception).isInstanceOf(NotFoundUserException.class);
        }
    }
}
