package com.prgrms.nabmart.domain.item.repository;

import static com.prgrms.nabmart.domain.item.support.ItemFixture.item;
import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.nabmart.base.TestQueryDslConfig;
import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.category.repository.SubCategoryRepository;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.ItemSortType;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import com.prgrms.nabmart.domain.order.OrderItem;
import com.prgrms.nabmart.domain.order.repository.OrderItemRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Import(TestQueryDslConfig.class)
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MainCategoryRepository mainCategoryRepository;
    @Autowired
    SubCategoryRepository subCategoryRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
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
                Item item = getSavedItem(i, 1, "0", 1, 1, 1, mainCategory, null);
                savedItems.add(item);
            }

            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findByMainCategoryOrderBy(
                mainCategory, 30L, Long.MAX_VALUE,
                ItemSortType.NEW, pageRequest);

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
                getSavedItem(i, (int) (Math.random() * 1000), "0", 0, 0, 0,
                    mainCategory, null);
            }

            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findByMainCategoryOrderBy(
                mainCategory, 100L, Long.MAX_VALUE,
                ItemSortType.DISCOUNT, pageRequest);

            // Then
            assertThat(items.size()).isEqualTo(5);
        }

        @Test
        @DisplayName("금액 높은 순으로 조회된다.")
        public void findByPriceLessThanAndMainCategoryOrderByPriceDescItemIdDesc() {
            //Given
            mainCategoryRepository.save(mainCategory);
            for (int i = 0; i < 50; i++) {
                getSavedItem(i, (int) (Math.random() * 1000), "0", 0, 0, 0,
                    mainCategory, null);
            }

            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findByMainCategoryOrderBy(
                mainCategory, 10000L, Long.MAX_VALUE,
                ItemSortType.HIGHEST_AMOUNT, pageRequest);

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
                getSavedItem(i, (int) (Math.random() * 1000), "0", 0, 0, 0,
                    mainCategory, null);
            }
            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findByMainCategoryOrderBy(
                mainCategory, 0L, Long.MAX_VALUE,
                ItemSortType.LOWEST_AMOUNT, pageRequest);

            // Then
            assertThat(items.size()).isEqualTo(5);
        }


        @Test
        @DisplayName("인기 순으로 조회된다.")
        public void findByOrderCount() {

            //Given
            mainCategoryRepository.save(mainCategory);
            subCategoryRepository.save(subCategory);
            for (int i = 0; i < 50; i++) {
                Item item = getSavedItem(i, (int) (Math.random() * 1000), "0", 0, 0, 0,
                    mainCategory, subCategory);
                OrderItem orderItem = new OrderItem(item, (50 - i));
                orderItemRepository.save(orderItem);
            }
            List<Long> expectedItemIds = List.of(1L, 2L, 3L, 4L, 5L);

            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findByMainCategoryOrderBy(
                mainCategory, 10000L, Long.MAX_VALUE,
                ItemSortType.POPULAR, pageRequest);

            // Then
            assertThat(items).map(Item::getItemId)
                .isEqualTo(expectedItemIds);
        }
    }

    @Nested
    @DisplayName("소카테고리 별로 아이템들이 ")
    class FindBySubCategoryByCriteria {

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
                Item item = getSavedItem(i, 1, "0", 1, 1, 1, mainCategory, subCategory);
                savedItems.add(item);
            }

            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findBySubCategoryOrderBy(
                mainCategory, subCategory, 30L, Long.MAX_VALUE,
                ItemSortType.NEW, pageRequest);

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
                getSavedItem(i, 0, "0", 0, (int) (Math.random() * 100), 0,
                    mainCategory,
                    subCategory);
            }

            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findBySubCategoryOrderBy(
                mainCategory, subCategory, 100L, Long.MAX_VALUE,
                ItemSortType.DISCOUNT, pageRequest);

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
                getSavedItem(i, (int) (Math.random() * 1000), "0", 0, 0, 0,
                    mainCategory, subCategory);
            }

            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findBySubCategoryOrderBy(
                mainCategory, subCategory, 100000L, Long.MAX_VALUE,
                ItemSortType.HIGHEST_AMOUNT, pageRequest);

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
                getSavedItem(i, (int) (Math.random() * 1000), "0", 0, 0, 0,
                    mainCategory, subCategory);
            }

            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findBySubCategoryOrderBy(
                mainCategory, subCategory, 0L, Long.MAX_VALUE,
                ItemSortType.LOWEST_AMOUNT, pageRequest);

            // Then
            assertThat(items.size()).isEqualTo(5);
        }

        @Test
        @DisplayName("인기 순으로 조회된다.")
        public void findByOrderCount() {

            //Given
            mainCategoryRepository.save(mainCategory);
            subCategoryRepository.save(subCategory);
            for (int i = 0; i < 50; i++) {
                Item item = getSavedItem(i, (int) (Math.random() * 1000), "0", 0, 0, 0,
                    mainCategory,
                    subCategory);
                OrderItem orderItem = new OrderItem(item, (50 - i));
                orderItemRepository.save(orderItem);
            }
            List<Long> expected = List.of(1L, 2L, 3L, 4L, 5L);

            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findBySubCategoryOrderBy(
                mainCategory, subCategory, 10000L, Long.MAX_VALUE,
                ItemSortType.POPULAR, pageRequest);

            List<Long> actual = items.stream()
                .map(Item::getItemId)
                .toList();
            // Then
            assertThat(items.size()).isEqualTo(5);
            assertThat(expected).usingRecursiveComparison()
                .isEqualTo(actual);
        }
    }

    @Test
    @DisplayName("아이템 삭제 시, 아이템 조회가 안된다.")
    public void deleteItem() {
        // Given
        MainCategory mainCategory = new MainCategory("main");
        SubCategory subCategory = new SubCategory(mainCategory, "sub");
        Item item = ItemFixture.item(mainCategory, subCategory);
        Item savedItem = itemRepository.save(item);

        // When
        itemRepository.deleteById(savedItem.getItemId());

        // Then
        Optional<Item> findItem = itemRepository.findByItemId(savedItem.getItemId());
        assertThat(findItem.isEmpty()).isEqualTo(true);
    }

    @Nested
    @DisplayName("increaseQuantity 메서드는")
    class IncreaseQuantityTest {

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            int increaseQuantity = 100;
            Item item = item();
            int originQuantity = item.getQuantity();

            mainCategoryRepository.save(item.getMainCategory());
            subCategoryRepository.save(item.getSubCategory());
            itemRepository.save(item);

            // When
            itemRepository.increaseQuantity(item.getItemId(), increaseQuantity);

            // Then
            Optional<Item> findItem = itemRepository.findById(item.getItemId());
            assertThat(findItem).isNotEmpty();
            assertThat(findItem.get().getQuantity()).isEqualTo(originQuantity + increaseQuantity);
        }
    }

    private Item getSavedItem(int i, int price, String description, int quantity, int discount,
        int maxBuyQuantity, MainCategory mainCategory, SubCategory subCategory) {

        Item item = new Item(
            "item" + (i + 1), price, description, quantity, discount, maxBuyQuantity,
            mainCategory, subCategory);
        itemRepository.save(item);
        return item;
    }
}
