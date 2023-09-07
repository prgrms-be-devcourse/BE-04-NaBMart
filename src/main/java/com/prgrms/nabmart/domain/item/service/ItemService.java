package com.prgrms.nabmart.domain.item.service;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.exception.NotFoundCategoryException;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.ItemSortType;
import com.prgrms.nabmart.domain.item.exception.NotFoundItemException;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.service.request.FindItemDetailCommand;
import com.prgrms.nabmart.domain.item.service.request.FindItemsByMainCategoryCommand;
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


    @Transactional(readOnly = true)
    public FindItemsResponse findItemsByMainCategory(
        FindItemsByMainCategoryCommand findItemsByMainCategoryCommand) {

        List<Item> items = findItemsByMainCategoryFrom(findItemsByMainCategoryCommand);
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

    private List<Item> findItemsByMainCategoryFrom(
        FindItemsByMainCategoryCommand findItemsByMainCategoryCommand) {

        Long lastIdx = findItemsByMainCategoryCommand.lastIdx();
        ItemSortType itemSortType = findItemsByMainCategoryCommand.itemSortType();
        PageRequest pageRequest = findItemsByMainCategoryCommand.pageRequest();
        String mainCategoryName = findItemsByMainCategoryCommand.mainCategoryName().toLowerCase();
        MainCategory mainCategory = mainCategoryRepository.findByName(mainCategoryName)
            .orElseThrow(() -> new NotFoundCategoryException("없는 대카테고리입니다."));

        switch (itemSortType) {
            case NEW -> {
                return itemRepository.findByItemIdLessThanAndMainCategoryOrderByItemIdDesc(
                    lastIdx, mainCategory, pageRequest);
            }
            case HIGHEST_AMOUNT -> {
                int price = lastIdx.intValue();
                return itemRepository.findByPriceLessThanAndMainCategoryOrderByPriceDescItemIdDesc(
                    price, mainCategory, pageRequest);
            }
            case LOWEST_AMOUNT -> {
                int price = lastIdx.intValue();
                return itemRepository.findByPriceGreaterThanAndMainCategoryOrderByPriceAscItemIdDesc(
                    price, mainCategory, pageRequest);
            }
            case DISCOUNT -> {
                int discountRate = lastIdx.intValue();
                return itemRepository.findByDiscountLessThanAndMainCategoryOrderByDiscountDescItemIdDesc(
                    discountRate, mainCategory, pageRequest);
            }
            default -> {
                Long totalOrderedQuantity = orderItemRepository.countByOrderItemId(lastIdx);
                return itemRepository.findByOrderedQuantityAndMainCategory(
                    totalOrderedQuantity, mainCategory, pageRequest);
            }
        }
    }
}
