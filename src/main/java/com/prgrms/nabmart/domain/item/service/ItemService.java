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
import com.prgrms.nabmart.domain.item.service.response.FindItemDetailResponse;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
import com.prgrms.nabmart.domain.order.repository.OrderItemRepository;
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
    private static final int NEW_PRODUCT_REFERENCE_WEEK = 2;

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
        return FindItemsResponse.from(itemRepository.findNewItemsOrderBy(findNewItemsCommand.lastIdx(),
            findNewItemsCommand.lastItemId(), findNewItemsCommand.sortType(), findNewItemsCommand.pageRequest()));
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
        List<Item> items = findHotItemsFrom(findHotItemsCommand);
        return FindItemsResponse.from(items);
    }

    private List<Item> findHotItemsFrom(FindHotItemsCommand findHotItemsCommand) {
        return switch (findHotItemsCommand.sortType()) {
            case NEW -> itemRepository.findHotItemOrderByItemIdDesc(findHotItemsCommand.lastIdx(),
                findHotItemsCommand.pageRequest());
            case LOWEST_AMOUNT ->
                itemRepository.findHotItemOrderByPriceAsc(findHotItemsCommand.lastIdx().intValue(),
                    findHotItemsCommand.pageRequest());
            case HIGHEST_AMOUNT ->
                itemRepository.findHotItemOrderByPriceDesc(findHotItemsCommand.lastIdx().intValue(),
                    findHotItemsCommand.pageRequest());
            case DISCOUNT -> itemRepository.findHotItemOrderByDiscountDesc(
                findHotItemsCommand.lastIdx().intValue(), findHotItemsCommand.pageRequest());
            default -> {
                int lastIdx = findHotItemsCommand.lastIdx().intValue();
                if (lastIdx != Integer.MAX_VALUE) {
                    lastIdx = orderItemRepository.countByOrderItemId(findHotItemsCommand.lastIdx())
                        .intValue();
                }
                yield itemRepository.findHotItemOrderByOrdersDesc(lastIdx,
                    findHotItemsCommand.pageRequest());
            }
        };
    }
}
