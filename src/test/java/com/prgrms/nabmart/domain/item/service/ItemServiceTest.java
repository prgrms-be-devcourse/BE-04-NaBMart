package com.prgrms.nabmart.domain.item.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.category.repository.SubCategoryRepository;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.ItemSortType;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.service.request.FindHotItemsCommand;
import com.prgrms.nabmart.domain.item.service.request.FindItemDetailCommand;
import com.prgrms.nabmart.domain.item.service.request.FindItemsByCategoryCommand;
import com.prgrms.nabmart.domain.item.service.request.FindNewItemsCommand;
import com.prgrms.nabmart.domain.item.service.response.FindItemDetailResponse;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import com.prgrms.nabmart.domain.order.repository.OrderItemRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private MainCategoryRepository mainCategoryRepository;

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private ItemService itemService;

    @Nested
    @DisplayName("findItemsByMainCategory 메서드 실행 시")
    class FindItemsByMainCategoryTests {

        MainCategory mainCategory = CategoryFixture.mainCategory();
        SubCategory subCategory = new SubCategory(mainCategory, "sub1");
        private static final int DEFAULT_PAGE_NUM = 0;
        private static final int DEFAULT_PAGE_SIZE = 3;

        @Test
        @DisplayName("최신 등록 순으로 조회")
        public void orderByLatest() {
            // Given
            List<Item> expectedItems = getItems();
            FindItemsByCategoryCommand findItemsByCategoryCommand = getFindItemsByCategoryCommand(
                ItemSortType.NEW);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(itemRepository.findByItemIdLessThanAndMainCategoryOrderByItemIdDesc(anyLong(),
                any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByCategory(
                findItemsByCategoryCommand);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        }

        @Test
        @DisplayName("할인율 높은 순으로 조회")
        public void orderByDiscountRateDesc() {
            // Given
            List<Item> items = getItems();
            List<Item> expectedItems = items.stream()
                .sorted(Comparator.comparing(Item::getDiscount).reversed())
                .toList();
            FindItemsByCategoryCommand findItemsByCategoryCommand = getFindItemsByCategoryCommand(
                ItemSortType.DISCOUNT);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(itemRepository.findByMainCategoryAndDiscountDesc(
                anyLong(), anyInt(), any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByCategory(
                findItemsByCategoryCommand);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        }

        @Test
        @DisplayName("금액 높은 순으로 조회")
        public void orderByPriceDesc() {
            // Given
            List<Item> expectedItems = getItems();
            FindItemsByCategoryCommand findItemsByCategoryCommand = getFindItemsByCategoryCommand(
                ItemSortType.HIGHEST_AMOUNT);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(itemRepository.findByMainCategoryAndPriceDesc(
                anyLong(), anyInt(), any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByCategory(
                findItemsByCategoryCommand);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        }

        @Test
        @DisplayName("금액 낮은 순으로 조회")
        public void orderByPriceAsc() {
            // Given
            List<Item> expectedItems = getItems();
            FindItemsByCategoryCommand findItemsByCategoryCommand = getFindItemsByCategoryCommand(
                ItemSortType.LOWEST_AMOUNT);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(itemRepository.findByByMainCategoryAndPriceAsc(
                anyLong(), anyInt(), any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByCategory(
                findItemsByCategoryCommand);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        }

        @Test
        @DisplayName("주문 많은 순으로 조회")
        public void orderByOrderedQuantity() {
            // Given
            List<Item> expectedItems = getItems();
            FindItemsByCategoryCommand findItemsByCategoryCommand = getFindItemsByCategoryCommand(
                ItemSortType.POPULAR);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(orderItemRepository.countByOrderItemId(anyLong())).thenReturn(Long.MAX_VALUE);
            when(itemRepository.findByOrderedQuantityAndMainCategory(
                anyLong(), anyLong(), any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByCategory(
                findItemsByCategoryCommand);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        }

        private FindItemsByCategoryCommand getFindItemsByCategoryCommand(
            ItemSortType itemSortType) {
            return new FindItemsByCategoryCommand(
                -1L, -1L, mainCategory.getName(), null,
                PageRequest.of(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE),
                itemSortType);
        }

        private List<Item> getItems() {
            Item item1 = Item.builder()
                .name("name1")
                .price(10)
                .quantity(10)
                .discount(1)
                .maxBuyQuantity(50)
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .build();

            Item item2 = Item.builder()
                .name("name2")
                .price(100)
                .quantity(10)
                .discount(10)
                .maxBuyQuantity(50)
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .build();

            Item item3 = Item.builder()
                .name("name3")
                .price(1000)
                .quantity(10)
                .discount(100)
                .maxBuyQuantity(50)
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .build();

            return List.of(item1, item2, item3);
        }
    }


    @Nested
    @DisplayName("findItemsBySubCategory 메서드 실행 시")
    class FindItemsBySubCategoryTests {

        MainCategory mainCategory = CategoryFixture.mainCategory();
        SubCategory subCategory = new SubCategory(mainCategory, "sub1");
        private static final int DEFAULT_PAGE_NUM = 0;
        private static final int DEFAULT_PAGE_SIZE = 3;

        @Test
        @DisplayName("최신 등록 순으로 조회")
        public void orderByLatest() {
            // Given
            List<Item> expectedItems = getItems();
            FindItemsByCategoryCommand findItemsByCategoryCommand = getFindItemsByCategoryCommand(
                ItemSortType.NEW);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(subCategoryRepository.findByName(any())).thenReturn(Optional.of(subCategory));
            when(itemRepository.findByItemIdLessThanAndMainCategoryAndSubCategoryOrderByItemIdDesc(
                anyLong(), any(), any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByCategory(
                findItemsByCategoryCommand);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        }

        @Test
        @DisplayName("할인율 높은 순으로 조회")
        public void orderByDiscountRateDesc() {
            // Given
            List<Item> items = getItems();
            List<Item> expectedItems = items.stream()
                .sorted(Comparator.comparing(Item::getDiscount).reversed())
                .toList();
            FindItemsByCategoryCommand findItemsByCategoryCommand = getFindItemsByCategoryCommand(
                ItemSortType.DISCOUNT);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(subCategoryRepository.findByName(any())).thenReturn(Optional.of(subCategory));
            when(
                itemRepository.findBySubCategoryAndDiscountDesc(
                    anyLong(), anyInt(), any(), any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByCategory(
                findItemsByCategoryCommand);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        }

        @Test
        @DisplayName("금액 높은 순으로 조회")
        public void orderByPriceDesc() {
            // Given
            List<Item> expectedItems = getItems();
            FindItemsByCategoryCommand findItemsByCategoryCommand = getFindItemsByCategoryCommand(
                ItemSortType.HIGHEST_AMOUNT);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(subCategoryRepository.findByName(any())).thenReturn(Optional.of(subCategory));
            when(
                itemRepository.findBySubCategoryAndPriceDesc(
                    anyLong(), anyInt(), any(), any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByCategory(
                findItemsByCategoryCommand);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        }

        @Test
        @DisplayName("금액 낮은 순으로 조회")
        public void orderByPriceAsc() {
            // Given
            List<Item> expectedItems = getItems();
            FindItemsByCategoryCommand findItemsByCategoryCommand = getFindItemsByCategoryCommand(
                ItemSortType.LOWEST_AMOUNT);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(subCategoryRepository.findByName(any())).thenReturn(Optional.of(subCategory));
            when(
                itemRepository.findBySubCategoryAndPriceAsc(
                    anyLong(), anyInt(), any(), any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByCategory(
                findItemsByCategoryCommand);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        }

        @Test
        @DisplayName("주문 많은 순으로 조회")
        public void orderByOrderedQuantity() {
            // Given
            List<Item> expectedItems = getItems();
            FindItemsByCategoryCommand findItemsByCategoryCommand = getFindItemsByCategoryCommand(
                ItemSortType.POPULAR);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(subCategoryRepository.findByName(any())).thenReturn(Optional.of(subCategory));
            when(orderItemRepository.countByOrderItemId(anyLong())).thenReturn(Long.MAX_VALUE);
            when(itemRepository.findByOrderedQuantityAndMainCategoryAndSubCategory(
                anyLong(), anyLong(), any(), any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByCategory(
                findItemsByCategoryCommand);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        }

        private FindItemsByCategoryCommand getFindItemsByCategoryCommand(
            ItemSortType itemSortType) {
            return new FindItemsByCategoryCommand(
                -1L, -1L, mainCategory.getName(), subCategory.getName(),
                PageRequest.of(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE),
                itemSortType);
        }

        private List<Item> getItems() {
            Item item1 = Item.builder()
                .name("name1")
                .price(10)
                .quantity(10)
                .discount(1)
                .maxBuyQuantity(50)
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .build();

            Item item2 = Item.builder()
                .name("name2")
                .price(100)
                .quantity(10)
                .discount(10)
                .maxBuyQuantity(50)
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .build();

            Item item3 = Item.builder()
                .name("name3")
                .price(1000)
                .quantity(10)
                .discount(100)
                .maxBuyQuantity(50)
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .build();

            return List.of(item1, item2, item3);
        }
    }

    @Nested
    @DisplayName("findItemDetail 메서드 실행 시")
    class FindItemDetailTests {

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            Item item = ItemFixture.item(CategoryFixture.mainCategory(),
                CategoryFixture.subCategory(CategoryFixture.mainCategory()));
            FindItemDetailCommand findItemDetailCommand = FindItemDetailCommand.from(
                item.getItemId());

            when(itemRepository.findById(item.getItemId())).thenReturn(Optional.of(item));

            // When
            FindItemDetailResponse response = itemService.findItemDetail(findItemDetailCommand);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.itemId()).isEqualTo(item.getItemId());
            assertThat(response.name()).isEqualTo(item.getName());
            assertThat(response.price()).isEqualTo(item.getPrice());
            assertThat(response.description()).isEqualTo(item.getDescription());
            assertThat(response.quantity()).isEqualTo(item.getQuantity());
            assertThat(response.rate()).isEqualTo(item.getRate());
            assertThat(response.discount()).isEqualTo(item.getDiscount());
            assertThat(response.maxBuyQuantity()).isEqualTo(item.getMaxBuyQuantity());
        }
    }

    @Nested
    @DisplayName("findNewItems 메서드 실행 시")
    class FindNewItemsTests {

        MainCategory mainCategory = CategoryFixture.mainCategory();
        SubCategory subCategory = new SubCategory(mainCategory, "sub");
        private static final int DEFAULT_PAGE_NUM = 0;
        private static final int DEFAULT_PAGE_SIZE = 3;

        Item item1 = Item.builder()
            .name("name1")
            .price(10)
            .quantity(10)
            .discount(1)
            .maxBuyQuantity(50)
            .mainCategory(mainCategory)
            .subCategory(subCategory)
            .build();

        Item item2 = Item.builder()
            .name("name2")
            .price(100)
            .quantity(10)
            .discount(10)
            .maxBuyQuantity(50)
            .mainCategory(mainCategory)
            .subCategory(subCategory)
            .build();

        Item item3 = Item.builder()
            .name("name3")
            .price(1000)
            .quantity(10)
            .discount(100)
            .maxBuyQuantity(50)
            .mainCategory(mainCategory)
            .subCategory(subCategory)
            .build();

        @Test
        @DisplayName("최신 등록 순으로 신상품 조회")
        public void orderByLatest() {
            // Given
            List<Item> expectedItems = List.of(item1, item2, item3);
            FindNewItemsCommand command = getFindNewItemsCommand(ItemSortType.NEW);

            when(itemRepository.findByCreatedAtAfterAndItemIdLessThanOrderByCreatedAtDesc(any(),
                any(), any()))
                .thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findNewItems(command);

            // Then
            assertThat(itemsResponse.items().get(0).name()).isEqualTo(item1.getName());
            assertThat(itemsResponse.items().get(1).name()).isEqualTo(item2.getName());
            assertThat(itemsResponse.items().get(2).name()).isEqualTo(item3.getName());
        }

        @Test
        @DisplayName("할인율 높은 순으로 신상품 조회")
        public void orderByDiscount() {
            // Given
            List<Item> expectedItems = List.of(item3, item2, item1);
            FindNewItemsCommand command = getFindNewItemsCommand(ItemSortType.DISCOUNT);

            when(
                itemRepository.findByCreatedAtAfterAndDiscountLessThanOrderByDiscountDescItemIdDesc(
                    any(), anyInt(), any()))
                .thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findNewItems(command);

            // Then
            assertThat(itemsResponse.items().get(0).name()).isEqualTo(item3.getName());
            assertThat(itemsResponse.items().get(1).name()).isEqualTo(item2.getName());
            assertThat(itemsResponse.items().get(2).name()).isEqualTo(item1.getName());
        }

        @Test
        @DisplayName("금액 높은 순으로 신상품 조회")
        public void orderByPriceDesc() {
            // Given
            List<Item> expectedItems = List.of(item3, item2, item1);
            FindNewItemsCommand command = getFindNewItemsCommand(ItemSortType.HIGHEST_AMOUNT);

            when(
                itemRepository.findByCreatedAtAfterAndPriceLessThanOrderByPriceDescItemIdDesc(any(),
                    anyInt(), any()))
                .thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findNewItems(command);

            // Then
            assertThat(itemsResponse.items().get(0).name()).isEqualTo(item3.getName());
            assertThat(itemsResponse.items().get(1).name()).isEqualTo(item2.getName());
            assertThat(itemsResponse.items().get(2).name()).isEqualTo(item1.getName());
        }

        @Test
        @DisplayName("금액 낮은 순으로 신상품 조회")
        public void orderByPriceAsc() {
            // Given
            List<Item> expectedItems = List.of(item1, item2, item3);
            FindNewItemsCommand command = getFindNewItemsCommand(ItemSortType.LOWEST_AMOUNT);

            when(itemRepository.findByCreatedAtAfterAndPriceGreaterThanOrderByPriceAscItemIdDesc(
                any(), anyInt(), any()))
                .thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findNewItems(command);

            // Then
            assertThat(itemsResponse.items().get(0).name()).isEqualTo(item1.getName());
            assertThat(itemsResponse.items().get(1).name()).isEqualTo(item2.getName());
            assertThat(itemsResponse.items().get(2).name()).isEqualTo(item3.getName());
        }

        @Test
        @DisplayName("주문 많은 순으로 신상품 조회")
        public void orderByOrderedQuantity() {
            // Given
            List<Item> expectedItems = List.of(item2, item3);
            FindNewItemsCommand command = getFindNewItemsCommand(ItemSortType.POPULAR);

            when(orderItemRepository.countByOrderItemId(anyLong()))
                .thenReturn(Long.MAX_VALUE);
            when(itemRepository.findNewItemOrderByOrders(any(), anyInt(), any()))
                .thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findNewItems(command);

            // Then
            assertThat(itemsResponse.items()).hasSize(DEFAULT_PAGE_SIZE - 1);
        }

        private FindNewItemsCommand getFindNewItemsCommand(ItemSortType itemSortType) {
            return new FindNewItemsCommand(-1L, PageRequest.of(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE),
                itemSortType);
        }
    }

    @Nested
    @DisplayName("findHotItems 메서드 실행 시")
    class FindHotItemsTests {

        MainCategory mainCategory = CategoryFixture.mainCategory();
        SubCategory subCategory = new SubCategory(mainCategory, "sub");
        private static final int DEFAULT_PAGE_NUM = 0;
        private static final int DEFAULT_PAGE_SIZE = 3;

        Item item1 = Item.builder()
            .name("name1")
            .price(10)
            .quantity(10)
            .discount(1)
            .maxBuyQuantity(50)
            .mainCategory(mainCategory)
            .subCategory(subCategory)
            .build();

        Item item2 = Item.builder()
            .name("name2")
            .price(100)
            .quantity(10)
            .discount(10)
            .maxBuyQuantity(50)
            .mainCategory(mainCategory)
            .subCategory(subCategory)
            .build();

        Item item3 = Item.builder()
            .name("name3")
            .price(1000)
            .quantity(10)
            .discount(100)
            .maxBuyQuantity(50)
            .mainCategory(mainCategory)
            .subCategory(subCategory)
            .build();

        @Test
        @DisplayName("최신 등록 순으로 인기 상품 조회")
        public void orderByLatest() {
            // Given
            List<Item> expectedItems = List.of(item1, item2, item3);
            FindHotItemsCommand command = getFindHotItemsCommand(ItemSortType.NEW);

            when(itemRepository.findHotItemOrderByItemIdDesc(anyLong(), any()))
                .thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findHotItems(command);

            // Then
            assertThat(itemsResponse.items().get(0).name()).isEqualTo(item1.getName());
            assertThat(itemsResponse.items().get(1).name()).isEqualTo(item2.getName());
            assertThat(itemsResponse.items().get(2).name()).isEqualTo(item3.getName());
        }

        @Test
        @DisplayName("할인율 높은 순으로 인기 상품 조회")
        public void orderByDiscount() {
            // Given
            List<Item> expectedItems = List.of(item3, item2, item1);
            FindHotItemsCommand command = getFindHotItemsCommand(ItemSortType.DISCOUNT);

            when(itemRepository.findHotItemOrderByDiscountDesc(anyInt(), any()))
                .thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findHotItems(command);

            // Then
            assertThat(itemsResponse.items().get(0).name()).isEqualTo(item3.getName());
            assertThat(itemsResponse.items().get(1).name()).isEqualTo(item2.getName());
            assertThat(itemsResponse.items().get(2).name()).isEqualTo(item1.getName());
        }

        @Test
        @DisplayName("금액 높은 순으로 인기 상품 조회")
        public void orderByPriceDesc() {
            // Given
            List<Item> expectedItems = List.of(item3, item2, item1);
            FindHotItemsCommand command = getFindHotItemsCommand(ItemSortType.HIGHEST_AMOUNT);

            when(itemRepository.findHotItemOrderByPriceDesc(anyInt(), any()))
                .thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findHotItems(command);

            // Then
            assertThat(itemsResponse.items().get(0).name()).isEqualTo(item3.getName());
            assertThat(itemsResponse.items().get(1).name()).isEqualTo(item2.getName());
            assertThat(itemsResponse.items().get(2).name()).isEqualTo(item1.getName());
        }

        @Test
        @DisplayName("금액 낮은 순으로 인기 상품 조회")
        public void orderByPriceAsc() {
            // Given
            List<Item> expectedItems = List.of(item1, item2, item3);
            FindHotItemsCommand command = getFindHotItemsCommand(ItemSortType.LOWEST_AMOUNT);

            when(itemRepository.findHotItemOrderByPriceAsc(anyInt(), any()))
                .thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findHotItems(command);

            // Then
            assertThat(itemsResponse.items().get(0).name()).isEqualTo(item1.getName());
            assertThat(itemsResponse.items().get(1).name()).isEqualTo(item2.getName());
            assertThat(itemsResponse.items().get(2).name()).isEqualTo(item3.getName());
        }

        @Test
        @DisplayName("주문 많은 순으로 인기 상품 조회")
        public void orderByOrderedQuantity() {
            // Given
            List<Item> expectedItems = List.of(item2, item3);
            FindHotItemsCommand command = getFindHotItemsCommand(ItemSortType.POPULAR);

            when(orderItemRepository.countByOrderItemId(anyLong()))
                .thenReturn(Long.MAX_VALUE);
            when(itemRepository.findHotItemOrderByOrdersDesc(anyInt(), any()))
                .thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findHotItems(command);

            // Then
            assertThat(itemsResponse.items()).hasSize(DEFAULT_PAGE_SIZE - 1);
        }

        private FindHotItemsCommand getFindHotItemsCommand(ItemSortType itemSortType) {
            return new FindHotItemsCommand(-1L, PageRequest.of(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE),
                itemSortType);
        }
    }
}
