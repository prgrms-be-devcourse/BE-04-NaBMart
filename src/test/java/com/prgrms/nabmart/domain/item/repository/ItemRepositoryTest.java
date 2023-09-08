package com.prgrms.nabmart.domain.item.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.category.repository.SubCategoryRepository;
import com.prgrms.nabmart.domain.item.Item;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MainCategoryRepository mainCategoryRepository;
    @Autowired
    SubCategoryRepository subCategoryRepository;
    @Autowired
    EntityManager entityManager;

    @AfterEach
    public void teardown() {
        this.itemRepository.deleteAll();
        this.entityManager
            .createNativeQuery("ALTER TABLE item ALTER COLUMN `item_id` RESTART WITH 1")
            .executeUpdate();
    }

    @Nested
    @DisplayName("대카테고리 별로 아이템들이 ")
    class FindByMainCategoryByCriteria {

        MainCategory mainCategory = CategoryFixture.mainCategory();
        SubCategory subCategory = CategoryFixture.subCategory(mainCategory);

        @Test
        @DisplayName("최신순으로 조회된다.")
        public void findByItemIdLessThanAndMainCategoryOrderByItemIdDesc() {
            //Given
            List<Item> savedItems = new ArrayList<>();
            mainCategoryRepository.save(mainCategory);
            subCategoryRepository.save(subCategory);
            for (int i = 0; i < 50; i++) {
                Item item = new Item("item" + (i + 1), 1, "0", 1, 1, 1, mainCategory, subCategory);
                itemRepository.save(item);
                savedItems.add(item);
            }

            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findByItemIdLessThanAndMainCategoryOrderByItemIdDesc(
                30L, mainCategory, pageRequest);

            // Then
            assertThat(items.size()).isEqualTo(5);
            List<Item> expectedItems = savedItems.subList(24, 29);
            Collections.reverse(expectedItems);
            List<String> actual = items.stream()
                .map(Item::getName)
                .toList();
            List<String> expected = expectedItems.stream()
                .map(Item::getName)
                .toList();

            assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
        }

        @Test
        @DisplayName("할인율 높은 순으로 조회된다.")
        public void findByItemIdLessThanAndMainCategoryOrderByItemIdDescDiscountDesc() {
            //Given
            mainCategoryRepository.save(mainCategory);
            subCategoryRepository.save(subCategory);
            for (int i = 0; i < 50; i++) {
                Item item = new Item("item" + (i + 1), 0, "0", 0, (int) (Math.random() * 100), 0,
                    mainCategory,
                    subCategory);
                itemRepository.save(item);
            }

            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findByDiscountLessThanAndMainCategoryOrderByDiscountDescItemIdDesc(
                100, mainCategory, pageRequest);

            // Then
            assertThat(items.size()).isEqualTo(5);
        }

        @Test
        @DisplayName("금액 높은 순으로 조회된다.")
        public void findByPriceLessThanAndMainCategoryOrderByPriceDescItemIdDesc() {
            //Given
            mainCategoryRepository.save(mainCategory);
            subCategoryRepository.save(subCategory);
            for (int i = 0; i < 50; i++) {
                Item item = new Item("item" + (i + 1), (int) (Math.random() * 1000), "0", 0, 0, 0,
                    mainCategory,
                    subCategory);
                itemRepository.save(item);
            }

            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findByPriceLessThanAndMainCategoryOrderByPriceDescItemIdDesc(
                10000, mainCategory, pageRequest);

            // Then
            assertThat(items.size()).isEqualTo(5);
        }

        @Test
        @DisplayName("금액 낮은 순으로 조회된다.")
        public void findByPriceGreaterThanAndMainCategoryOrderByPriceAscItemIdDesc() {
            //Given
            mainCategoryRepository.save(mainCategory);
            subCategoryRepository.save(subCategory);
            for (int i = 0; i < 50; i++) {
                Item item = new Item("item" + (i + 1), (int) (Math.random() * 1000), "0", 0, 0, 0,
                    mainCategory,
                    subCategory);
                itemRepository.save(item);
            }

            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findByPriceGreaterThanAndMainCategoryOrderByPriceAscItemIdDesc(
                0, mainCategory, pageRequest);

            // Then
            assertThat(items.size()).isEqualTo(5);
        }
    }
}
