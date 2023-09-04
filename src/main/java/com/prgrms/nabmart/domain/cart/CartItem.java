package com.prgrms.nabmart.domain.cart;

import static java.util.Objects.isNull;

import com.prgrms.nabmart.domain.cart.exception.InvalidCartItemQuantityException;
import com.prgrms.nabmart.domain.cart.exception.NotExistsCartException;
import com.prgrms.nabmart.domain.item.domain.Item;
import com.prgrms.nabmart.domain.item.exception.NotExistsItemException;
import com.prgrms.nabmart.global.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cart_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseTimeEntity {

    private static final int MIN_QUANTITY = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private boolean isChecked;

    @Builder
    public CartItem(Cart cart, Item item, int quantity) {
        validateCart(cart);
        validateItem(item);
        validateQuantity(quantity);
        this.cart = cart;
        this.item = item;
        this.quantity = quantity;
        this.isChecked = true;
    }

    public void validateCart(Cart cart) {
        if (isNull(cart)) {
            throw new NotExistsCartException("Cart 가 존재하지 않습니다.");
        }
    }

    public void validateItem(Item item) {
        if (isNull(item)) {
            throw new NotExistsItemException("Item 이 존재하지 않습니다.");
        }
    }

    public void validateQuantity(int quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new InvalidCartItemQuantityException("수량은 음수가 될 수 없습니다.");
        }
    }
}
