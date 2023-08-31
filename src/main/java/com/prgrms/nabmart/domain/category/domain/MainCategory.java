package com.prgrms.nabmart.domain.category.domain;

import com.prgrms.nabmart.domain.item.domain.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mainCategoryId;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "mainCategory")
    private List<Item> items = new ArrayList<>();

    public MainCategory(String name) {
        this.name = name;
    }
}
