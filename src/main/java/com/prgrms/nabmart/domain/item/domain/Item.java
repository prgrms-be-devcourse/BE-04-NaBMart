package com.prgrms.nabmart.domain.item.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;
    private String name;
    private Integer price;
    private String description;
    private Integer quantity;
    private Double rate;
    private Integer reviewCount;
    private Integer discount;
    private Integer like;
    private Integer maxBuyQuantity;

    @Builder
    public Item(String name, Integer price, String description, Integer quantity, Double rate,
        Integer reviewCount, Integer discount, Integer like, Integer maxBuyQuantity) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.rate = rate;
        this.reviewCount = reviewCount;
        this.discount = discount;
        this.like = like;
        this.maxBuyQuantity = maxBuyQuantity;
    }
}
