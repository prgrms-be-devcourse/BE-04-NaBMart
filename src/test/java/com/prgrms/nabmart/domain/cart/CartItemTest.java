package com.prgrms.nabmart.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.prgrms.nabmart.domain.cart.exception.InvalidCartItemException;
import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.global.fixture.CartFixture;
import com.prgrms.nabmart.global.fixture.CartItemFixture;
import com.prgrms.nabmart.global.fixture.ItemFixture;
import com.prgrms.nabmart.global.fixture.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CartItemTest {

    User givenUser;
    Cart givenCart;
    Item givenItem;
    MainCategory givenMainCategory;
    SubCategory givenSubCategory;
    CartItem givenCartItem;
    int givenQuantity;

    @BeforeEach
    void setUp() {
        givenUser = UserFixture.user();
        givenCart = CartFixture.cart(givenUser);
        givenMainCategory = CategoryFixture.mainCategory();
        givenSubCategory = CategoryFixture.subCategory(givenMainCategory);
        givenItem = ItemFixture.item(givenMainCategory, givenSubCategory);
        givenQuantity = 10;
        givenCartItem = CartItemFixture.cartItem(givenCart, givenItem, givenQuantity);
    }

    @Nested
    @DisplayName("장바구니 상품 생성 시")
    class NewCartItemTest {

        @Test
        @DisplayName("성공")
        void newCartItem() {
            // given

            // when

            // then
            assertThat(givenCartItem.getCart()).isEqualTo(givenCart);
            assertThat(givenCartItem.getItem()).isEqualTo(givenItem);
            assertThat(givenCartItem.getQuantity()).isEqualTo(givenQuantity);
        }

        @Test
        @DisplayName("예외 : Cart 가 null")
        void throwExceptionWhenCartIsNull() {
            // given
            Cart nullCart = null;

            // when
            Exception exception = catchException(() ->
                CartItem.builder()
                    .cart(nullCart)
                    .item(givenItem)
                    .quantity(givenQuantity)
                    .build()
            );

            // then
            assertThat(exception).isInstanceOf(InvalidCartItemException.class);
        }

        @Test
        @DisplayName("예외 : Item 이 null")
        void throwExceptionWhenItemIsNull() {
            // given
            Item nullItem = null;

            // when
            Exception exception = catchException(() ->
                CartItem.builder()
                    .cart(givenCart)
                    .item(nullItem)
                    .quantity(givenQuantity)
                    .build()
            );

            // then
            assertThat(exception).isInstanceOf(InvalidCartItemException.class);
        }

        @Test
        @DisplayName("예외 : Quantity 가 음수")
        void throwExceptionWhenQuantityIsOutOfRange() {
            // given
            int nullQuantity = -1;

            // when
            Exception exception = catchException(() ->
                CartItem.builder()
                    .cart(givenCart)
                    .item(givenItem)
                    .quantity(nullQuantity)
                    .build()
            );

            // then
            assertThat(exception).isInstanceOf(InvalidCartItemException.class);
        }
    }
}
