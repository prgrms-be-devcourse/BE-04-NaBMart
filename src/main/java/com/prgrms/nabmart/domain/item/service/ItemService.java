package com.prgrms.nabmart.domain.item.service;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.exception.NotFoundCategoryException;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.ItemSortType;
import com.prgrms.nabmart.domain.item.exception.NotFoundItemException;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.service.request.FindItemsByMainCategoryCommand;
import com.prgrms.nabmart.domain.item.service.request.FindItemDetailCommand;
import com.prgrms.nabmart.domain.item.service.response.FindItemDetailResponse;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final MainCategoryRepository mainCategoryRepository;


    @Transactional(readOnly = true)
    public FindItemsResponse findItemsByMainCategory(
        FindItemsByMainCategoryCommand findItemsByMainCategoryCommand) {

        List<Item> items = findItemsByMainCategoryFrom(
            findItemsByMainCategoryCommand);
        return FindItemsResponse.from(items);
    }


    private List<Item> findItemsByMainCategoryFrom(
        FindItemsByMainCategoryCommand findItemsByMainCategoryCommand) {

        Long previousItemId = findItemsByMainCategoryCommand.previousItemId();
        ItemSortType itemSortType = findItemsByMainCategoryCommand.itemSortType();
        PageRequest pageRequest = findItemsByMainCategoryCommand.pageRequest();
        String mainCategoryName = findItemsByMainCategoryCommand.mainCategoryName().toLowerCase();
        MainCategory mainCategory = mainCategoryRepository.findByName(mainCategoryName)
            .orElseThrow(() -> new NotFoundCategoryException("없는 대카테고리입니다."));

        switch (itemSortType) {
            case NEW -> {
                return itemRepository.findByItemIdLessThanAndMainCategoryOrderByItemIdDesc(
                    previousItemId, mainCategory, pageRequest);
            }

            default -> {
                return new ArrayList<>();
            }
        }
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
}
