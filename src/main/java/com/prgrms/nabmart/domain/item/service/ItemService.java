package com.prgrms.nabmart.domain.item.service;

import com.prgrms.nabmart.domain.item.domain.Item;
import com.prgrms.nabmart.domain.item.domain.ItemSortType;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.service.request.FindNewItemsCommand;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse.FindItemResponse;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse.PageInfoResponse;
import com.prgrms.nabmart.domain.review.Review;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public FindItemsResponse findNewItems(FindNewItemsCommand findNewItemsCommand) {
        int lastItemId = findNewItemsCommand.lastItemId();
        int pageSize = findNewItemsCommand.pageSize();

        List<Item> items = findNewItemsSorted(findNewItemsCommand.sortType());

        List<FindItemResponse> findItemResponses = items.stream().map(item -> new FindItemResponse(
            item.getItemId(),
            item.getName(),
            item.getPrice(),
            item.getDiscount(),
            item.getReviews().size(),
            item.getLikeItems().size(),
            item.getRate()
        )).toList();
        PageInfoResponse pageInfoResponse = new PageInfoResponse(items.getNumber(),
            items.getTotalPages(), items.getTotalElements());
        return FindItemsResponse.of(pageInfoResponse, findItemResponses);
    }

    private List<Item> findNewItemsSorted(final ItemSortType itemSortType) {
        LocalDateTime createdAt = LocalDateTime.now().minus(2, ChronoUnit.WEEKS);

        return switch (itemSortType) {
            case HIGHEST_AMOUNT ->
                itemRepository.findByCreatedAtAfterOrderByPriceDesc(createdAt);
            case LOWEST_AMOUNT ->
                itemRepository.findByCreatedAtAfterOrderByPriceAsc(createdAt);
            case NEW ->
                itemRepository.findByCreatedAtAfterOrderByCreatedAtDesc(createdAt);
            case DISCOUNT ->
                itemRepository.findByCreatedAtAfterOrderByDiscountDesc(createdAt);
            case POPULAR -> itemRepository.findNewItemsOrderByPopularity(createdAt);
            default -> itemRepository.findByCreatedAtAfter(createdAt);
        };
    }

    private Page<Item> fetchPages(Long lastItemId, int size, List<Item> items) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return
    }

}
