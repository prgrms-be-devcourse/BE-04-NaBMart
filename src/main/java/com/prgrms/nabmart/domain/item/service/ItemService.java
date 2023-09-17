package com.prgrms.nabmart.domain.item.service;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.exception.NotFoundCategoryException;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.category.repository.SubCategoryRepository;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.ItemSortType;
import com.prgrms.nabmart.domain.item.exception.NotFoundItemException;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.service.request.FindHotItemsCommand;
import com.prgrms.nabmart.domain.item.service.request.FindItemDetailCommand;
import com.prgrms.nabmart.domain.item.service.request.FindItemsByCategoryCommand;
import com.prgrms.nabmart.domain.item.service.request.FindNewItemsCommand;
import com.prgrms.nabmart.domain.item.service.request.RegisterItemCommand;
import com.prgrms.nabmart.domain.item.service.request.UpdateItemCommand;
import com.prgrms.nabmart.domain.item.service.response.FindItemDetailResponse;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
import com.prgrms.nabmart.domain.item.service.response.FindNewItemsResponse;
import com.prgrms.nabmart.domain.item.service.response.FindNewItemsResponse.FindNewItemResponse;
import com.prgrms.nabmart.domain.item.service.response.ItemRedisDto;
import com.prgrms.nabmart.domain.order.repository.OrderItemRepository;
import com.prgrms.nabmart.domain.review.service.RedisCacheService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ItemCacheService itemCacheService;
    private final RedisCacheService redisCacheService;

    @Transactional
    public Long saveItem(RegisterItemCommand registerItemCommand) {
        Long mainCategoryId = registerItemCommand.mainCategoryId();
        Long subCategoryId = registerItemCommand.subCategoryId();

        MainCategory mainCategory = getMainCategoryById(mainCategoryId);
        SubCategory subCategory = getSubCategoryById(subCategoryId);
        Item item = Item.builder()
            .name(registerItemCommand.name())
            .price(registerItemCommand.price())
            .description(registerItemCommand.description())
            .quantity(registerItemCommand.quantity())
            .discount(registerItemCommand.discount())
            .maxBuyQuantity(registerItemCommand.maxBuyQuantity())
            .mainCategory(mainCategory)
            .subCategory(subCategory)
            .build();

        Item savedItem = itemRepository.save(item);
        itemCacheService.saveNewItem(ItemRedisDto.from(savedItem));
        return savedItem.getItemId();
    }

    @Transactional(readOnly = true)
    public FindItemsResponse findItemsByCategory(
        FindItemsByCategoryCommand findItemsByCategoryCommand) {
        String subCategoryName = findItemsByCategoryCommand.subCategoryName();
        if (subCategoryName == null || subCategoryName.isBlank()) {
            return FindItemsResponse.from(
                findItemsByMainCategoryFrom(findItemsByCategoryCommand)
            );
        }
        List<Item> items = findItemsBySubCategoryFrom(findItemsByCategoryCommand);
        return FindItemsResponse.from(items);
    }

    @Transactional(readOnly = true)
    public FindItemDetailResponse findItemDetail(FindItemDetailCommand findItemDetailCommand) {
        Item item = itemRepository.findById(findItemDetailCommand.itemId())
            .orElseThrow(() -> new NotFoundItemException("존재하지 않는 상품입니다."));

        return FindItemDetailResponse.of(
            item.getItemId(),
            item.getName(),
            item.getPrice(),
            item.getDescription(),
            item.getQuantity(),
            item.getRate(),
            item.getReviews().size(),
            item.getDiscount(),
            item.getLikeItems().size(),
            item.getMaxBuyQuantity()
        );
    }

    @Transactional(readOnly = true)
    public FindItemsResponse findNewItems(FindNewItemsCommand findNewItemsCommand) {
        return FindItemsResponse.from(
            itemRepository.findNewItemsOrderBy(findNewItemsCommand.lastIdx(),
                findNewItemsCommand.lastItemId(), findNewItemsCommand.sortType(),
                findNewItemsCommand.pageRequest()));
    }

    @Transactional(readOnly = true)
    public FindNewItemsResponse findNewItemsWithRedis(ItemSortType sortType) {
        List<ItemRedisDto> itemRedisDtos = itemCacheService.getNewItems();
        List<FindNewItemResponse> items = itemRedisDtos.stream().map(item -> FindNewItemResponse.of(
            item.itemId(),
            item.name(),
            item.price(),
            item.discount(),
            redisCacheService.getTotalNumberOfReviewsByItemId(item.itemId(), "reviewCount_Item_" + item.itemId())
        )).toList();

        return FindNewItemsResponse.from(sortNewItems(items, sortType));
    }

    private List<FindNewItemResponse> sortNewItems(List<FindNewItemResponse> items, ItemSortType sortType) {
        List<FindNewItemResponse> sortedItems = new ArrayList<>(items);
        switch (sortType) {
            case LOWEST_AMOUNT -> sortedItems.sort(Comparator.comparingInt(FindNewItemResponse::price));
            case HIGHEST_AMOUNT -> sortedItems.sort(Comparator.comparingInt(FindNewItemResponse::price).reversed());
            case NEW -> sortedItems.sort(Comparator.comparingLong(FindNewItemResponse::itemId).reversed());
            case DISCOUNT -> sortedItems.sort(Comparator.comparingInt(FindNewItemResponse::discount).reversed());
            default -> sortedItems.sort(Comparator.comparingLong(FindNewItemResponse::reviewCount).reversed());
        }
        return sortedItems;
    }

    @Transactional
    public void updateItem(UpdateItemCommand updateItemCommand) {
        Long itemId = updateItemCommand.itemId();
        Long mainCategoryId = updateItemCommand.mainCategoryId();
        Long subCategoryId = updateItemCommand.subCategoryId();

        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundItemException("해당 Id의 아이템은 존재하지 않습니다."));
        MainCategory mainCategory = getMainCategoryById(mainCategoryId);
        SubCategory subCategory = getSubCategoryById(subCategoryId);
        item.updateItem(updateItemCommand.name(), updateItemCommand.price(),
            updateItemCommand.quantity(), updateItemCommand.description(),
            mainCategory, subCategory, updateItemCommand.discount());
    }

    @Transactional
    public void deleteById(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    private SubCategory getSubCategoryById(Long subCategoryId) {
        SubCategory subCategory = null;
        if (subCategoryId != null) {
            subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new NotFoundCategoryException("없는 소카테고리입니다."));
        }
        return subCategory;
    }

    private MainCategory getMainCategoryById(Long mainCategoryId) {
        MainCategory mainCategory = null;
        if (mainCategoryId != null) {
            mainCategory = mainCategoryRepository.findById(mainCategoryId)
                .orElseThrow(() -> new NotFoundCategoryException("없는 대카테고리입니다."));
        }
        return mainCategory;
    }

    private List<Item> findItemsByMainCategoryFrom(
        FindItemsByCategoryCommand findItemsByCategoryCommand) {

        Long lastIdx = findItemsByCategoryCommand.lastIdx();
        Long option = findItemsByCategoryCommand.option();
        ItemSortType itemSortType = findItemsByCategoryCommand.itemSortType();
        PageRequest pageRequest = findItemsByCategoryCommand.pageRequest();
        String mainCategoryName = findItemsByCategoryCommand.mainCategoryName().toLowerCase();
        MainCategory mainCategory = mainCategoryRepository.findByName(mainCategoryName)
            .orElseThrow(() -> new NotFoundCategoryException("없는 대카테고리입니다."));

        switch (itemSortType) {
            case NEW -> {
                return itemRepository.findByItemIdLessThanAndMainCategoryOrderByItemIdDesc(
                    lastIdx, mainCategory, pageRequest);
            }
            case HIGHEST_AMOUNT -> {
                int price = option.intValue();
                return itemRepository.findByMainCategoryAndPriceDesc(
                    lastIdx, price, mainCategory, pageRequest);
            }
            case LOWEST_AMOUNT -> {
                int price = option.intValue();
                return itemRepository.findByByMainCategoryAndPriceAsc(
                    lastIdx, price, mainCategory, pageRequest);
            }
            case DISCOUNT -> {
                int discountRate = option.intValue();
                return itemRepository.findByMainCategoryAndDiscountDesc(
                    lastIdx, discountRate, mainCategory, pageRequest);
            }
            default -> {
                Long totalOrderedQuantity = ItemSortType.POPULAR.getDefaultValue();
                if (lastIdx != ItemSortType.POPULAR.getDefaultValue()) {
                    totalOrderedQuantity = orderItemRepository.countByOrderItemId(lastIdx);
                }
                return itemRepository.findByOrderedQuantityAndMainCategory(
                    lastIdx, totalOrderedQuantity, mainCategory, pageRequest);
            }
        }
    }

    private List<Item> findItemsBySubCategoryFrom(
        FindItemsByCategoryCommand findItemsByCategoryCommand) {

        Long lastIdx = findItemsByCategoryCommand.lastIdx();
        Long option = findItemsByCategoryCommand.option();
        ItemSortType itemSortType = findItemsByCategoryCommand.itemSortType();
        PageRequest pageRequest = findItemsByCategoryCommand.pageRequest();
        String mainCategoryName = findItemsByCategoryCommand.mainCategoryName().toLowerCase();
        String subCategoryName = findItemsByCategoryCommand.subCategoryName().toLowerCase();
        MainCategory mainCategory = mainCategoryRepository.findByName(mainCategoryName)
            .orElseThrow(() -> new NotFoundCategoryException("없는 대카테고리입니다."));
        SubCategory subCategory = subCategoryRepository.findByName(subCategoryName)
            .orElseThrow(() -> new NotFoundCategoryException("없는 소카테고리입니다."));

        switch (itemSortType) {
            case NEW -> {
                return itemRepository.findByItemIdLessThanAndMainCategoryAndSubCategoryOrderByItemIdDesc(
                    lastIdx, mainCategory, subCategory, pageRequest);
            }
            case HIGHEST_AMOUNT -> {
                int price = option.intValue();
                return itemRepository.findBySubCategoryAndPriceDesc(
                    lastIdx, price, mainCategory, subCategory, pageRequest);
            }
            case LOWEST_AMOUNT -> {
                int price = option.intValue();
                return itemRepository.findBySubCategoryAndPriceAsc(
                    lastIdx, price, mainCategory, subCategory, pageRequest);
            }
            case DISCOUNT -> {
                int discountRate = option.intValue();
                return itemRepository.findBySubCategoryAndDiscountDesc(
                    lastIdx, discountRate, mainCategory, subCategory, pageRequest);
            }
            default -> {
                Long totalOrderedQuantity = ItemSortType.POPULAR.getDefaultValue();
                if (lastIdx != ItemSortType.POPULAR.getDefaultValue()) {
                    totalOrderedQuantity = orderItemRepository.countByOrderItemId(lastIdx);
                }
                return itemRepository.findByOrderedQuantityAndMainCategoryAndSubCategory(
                    lastIdx, totalOrderedQuantity, mainCategory, subCategory, pageRequest);
            }
        }
    }

    @Transactional(readOnly = true)
    public FindItemsResponse findHotItems(FindHotItemsCommand findHotItemsCommand) {
        List<Item> items = itemRepository.findHotItemsOrderBy(findHotItemsCommand.lastIdx(),
            findHotItemsCommand.lastItemId(), findHotItemsCommand.sortType(),
            findHotItemsCommand.pageRequest());
        return FindItemsResponse.from(items);
    }
}
