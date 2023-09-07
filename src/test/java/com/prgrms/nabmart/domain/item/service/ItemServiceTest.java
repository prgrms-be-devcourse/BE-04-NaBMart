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
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.ItemSortType;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.service.request.FindItemDetailCommand;
import com.prgrms.nabmart.domain.item.service.request.FindItemsByMainCategoryCommand;
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
            FindItemsByMainCategoryCommand findItemsByMainCategoryCommand = getFindItemsByMainCategoryCommand(
                ItemSortType.NEW);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(itemRepository.findByItemIdLessThanAndMainCategoryOrderByItemIdDesc(anyLong(),
                any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByMainCategory(
                findItemsByMainCategoryCommand);

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
            FindItemsByMainCategoryCommand findItemsByMainCategoryCommand = getFindItemsByMainCategoryCommand(
                ItemSortType.DISCOUNT);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(itemRepository.findByDiscountLessThanAndMainCategoryOrderByDiscountDescItemIdDesc(
                anyInt(), any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByMainCategory(
                findItemsByMainCategoryCommand);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        }

        @Test
        @DisplayName("금액 높은 순으로 조회")
        public void orderByPriceDesc() {
            // Given
            List<Item> items = getItems();
            List<Item> expectedItems = items.stream()
                .sorted(Comparator.comparing(Item::getDiscount).reversed())
                .toList();
            FindItemsByMainCategoryCommand findItemsByMainCategoryCommand = getFindItemsByMainCategoryCommand(
                ItemSortType.HIGHEST_AMOUNT);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(itemRepository.findByPriceLessThanAndMainCategoryOrderByPriceDescItemIdDesc(
                anyInt(), any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByMainCategory(
                findItemsByMainCategoryCommand);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        }

        @Test
        @DisplayName("금액 낮은 순으로 조회")
        public void orderByPriceAsc() {
            // Given
            List<Item> items = getItems();
            List<Item> expectedItems = items.stream()
                .sorted(Comparator.comparing(Item::getDiscount).reversed())
                .toList();
            FindItemsByMainCategoryCommand findItemsByMainCategoryCommand = getFindItemsByMainCategoryCommand(
                ItemSortType.LOWEST_AMOUNT);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(itemRepository.findByPriceGreaterThanAndMainCategoryOrderByPriceAscItemIdDesc(
                anyInt(), any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByMainCategory(
                findItemsByMainCategoryCommand);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        }

        @Test
        @DisplayName("주문 많은 순으로 조회")
        public void orderByOrderedQuantity() {
            // Given
            List<Item> items = getItems();
            List<Item> expectedItems = items.stream()
                .sorted(Comparator.comparing(Item::getDiscount).reversed())
                .toList();
            FindItemsByMainCategoryCommand findItemsByMainCategoryCommand = getFindItemsByMainCategoryCommand(
                ItemSortType.POPULAR);

            when(mainCategoryRepository.findByName(any())).thenReturn(Optional.of(mainCategory));
            when(orderItemRepository.countByOrderItemId(anyLong())).thenReturn(Long.MAX_VALUE);
            when(itemRepository.findByOrderedQuantityAndMainCategory(
                anyLong(), any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByMainCategory(
                findItemsByMainCategoryCommand);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        }

        private FindItemsByMainCategoryCommand getFindItemsByMainCategoryCommand(
            ItemSortType itemSortType) {
            return new FindItemsByMainCategoryCommand(
                -1L, mainCategory.getName(), PageRequest.of(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE),
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
            FindItemDetailCommand command = FindItemDetailCommand.from(item.getItemId());

            when(itemRepository.findById(item.getItemId())).thenReturn(Optional.of(item));

            // When
            FindItemDetailResponse response = itemService.findItemDetail(command);

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
}
