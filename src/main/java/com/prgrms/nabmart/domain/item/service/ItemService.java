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
        PageRequest pageRequest = PageRequest.of(0, findNewItemsCommand.pageSize());

        Page<Item> items = findNewItemsSorted(findNewItemsCommand.sortType(), pageRequest, findNewItemsCommand.lastItemId());

        List<FindItemResponse> findItemResponses = items.stream().map(item -> new FindItemResponse(
            item.getItemId(),
            item.getName(),
            item.getPrice(),
            item.getDiscount(),
            item.getReviews().size(),
            item.getLikeItems().size(),
            item.getReviews().stream().mapToDouble(Review::getRate).average()
                .orElse(0.0)
        )).toList();
        PageInfoResponse pageInfoResponse = new PageInfoResponse(items.getNumber(),
            items.getTotalPages(), items.getTotalElements());
        return FindItemsResponse.of(pageInfoResponse, findItemResponses);
    }

    private Page<Item> findNewItemsSorted(final ItemSortType itemSortType,
        final PageRequest pageRequest, final int lastItemId) {
        LocalDateTime createdAt = LocalDateTime.now().minus(2, ChronoUnit.WEEKS);

        return switch (itemSortType) {
            case HIGHEST_AMOUNT ->
                itemRepository.findByCreatedAtAfterOrderByPriceDesc(createdAt, pageRequest);
            case LOWEST_AMOUNT ->
                itemRepository.findByCreatedAtAfterOrderByPriceAsc(createdAt, pageRequest);
            case NEW ->
                itemRepository.findByCreatedAtAfterOrderByCreatedAtDesc(createdAt, pageRequest);
            case DISCOUNT ->
                itemRepository.findByCreatedAtAfterOrderByDiscountDesc(createdAt, pageRequest);
            case POPULAR -> itemRepository.findNewItemsOrderByPopularity(createdAt, pageRequest);
            default -> itemRepository.findByCreatedAtAfter(createdAt, pageRequest);
        };
    }

}
