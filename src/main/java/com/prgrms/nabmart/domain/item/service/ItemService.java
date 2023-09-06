package com.prgrms.nabmart.domain.item.service;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.exception.NotFoundCategoryException;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
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
    public FindItemsResponse findItemsByMainCategory(Long previousItemId, Long mainCategoryId,
        int pageSize) {

        MainCategory mainCategory = mainCategoryRepository.findById(mainCategoryId)
            .orElseThrow(() -> new NotFoundCategoryException("없는 대카테고리입니다."));
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        List<Item> items = itemRepository.findByItemIdLessThanAndMainCategoryOrderByItemIdDesc(
            previousItemId, mainCategory, pageRequest);

        return FindItemsResponse.from(items);
    }
}
