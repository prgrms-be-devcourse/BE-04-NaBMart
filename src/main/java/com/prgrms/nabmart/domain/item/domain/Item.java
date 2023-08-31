package com.prgrms.nabmart.domain.item.domain;

import com.prgrms.nabmart.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(nullable = false)
    private int quantity;

    @ColumnDefault("0")
    @Column(nullable = false)
    private int discount;

    @ColumnDefault("10")
    @Column(nullable = false)
    private int maxBuyQuantity;

    @Builder
    public Item(String name, int price, String description, int quantity, int discount,
        int maxBuyQuantity) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.discount = discount;
        this.maxBuyQuantity = maxBuyQuantity;
    }
}
