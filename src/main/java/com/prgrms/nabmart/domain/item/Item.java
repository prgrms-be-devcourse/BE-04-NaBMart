package com.prgrms.nabmart.domain.item;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
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

    @Builder
    public Item(String name, int price, String description, int quantity, int discount,
        int maxBuyQuantity, MainCategory mainCategory, SubCategory subCategory) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.discount = discount;
        this.maxBuyQuantity = maxBuyQuantity;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
    }

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