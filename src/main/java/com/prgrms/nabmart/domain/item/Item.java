package com.prgrms.nabmart.domain.item;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.item.exception.InvalidItemException;
import com.prgrms.nabmart.domain.order.OrderItem;
import com.prgrms.nabmart.domain.review.Review;
import com.prgrms.nabmart.global.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column
    private String description;

    @Column
    private double rate;

    @Column(nullable = false)
    private int quantity;

    @ColumnDefault("0")
    @Column(nullable = false)
    private int discount;

    @ColumnDefault("10")
    @Column(nullable = false)
    private int maxBuyQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<LikeItem> likeItems = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Item(String name, int price, String description, int quantity, int discount,
        int maxBuyQuantity, MainCategory mainCategory, SubCategory subCategory) {
        validateName(name);
        validatePrice(price);
        validateQuantity(quantity);
        validateDiscount(discount);
        validateMaxBuyQuantity(maxBuyQuantity);
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.discount = discount;
        this.maxBuyQuantity = maxBuyQuantity;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
    }


    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidItemException("상품명은 필수 항목입니다.");
        }
    }

    private void validatePrice(int price) {
        if (price < 0) {
            throw new InvalidItemException("상품가격은 0원 이상이어야 합니다.");
        }
    }

    private void validateDiscount(int discount) {
        if (discount < 0) {
            throw new InvalidItemException("상품 할인율은 0 이상이어야 합니다.");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new InvalidItemException("상품 재고수량은 0개 이상이어야 합니다.");
        }
    }

    private void validateMaxBuyQuantity(int maxBuyQuantity) {
        if (maxBuyQuantity < 0) {
            throw new InvalidItemException("상품 최대 주문수량은 0개 이상이어야 합니다.");
        }
    }
}
