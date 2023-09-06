package com.prgrms.nabmart.domain.item.controller;

import com.prgrms.nabmart.domain.item.domain.ItemSortType;
import com.prgrms.nabmart.domain.item.service.ItemService;
import com.prgrms.nabmart.domain.item.service.request.FindNewItemsCommand;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/new")
    public ResponseEntity<FindItemsResponse> findNewItems(@RequestParam final int lastItemId,
        @RequestParam final int pageSize, @RequestParam(required = false) final String sort
    ) {
        ItemSortType sortType = ItemSortType.DEFAULT;
        if (sort != null && !sort.isEmpty()) {
            sortType = ItemSortType.valueOf(sort);
        }

        FindNewItemsCommand findNewItemsCommand = FindNewItemsCommand.of(lastItemId, pageSize, sortType);
        return ResponseEntity.ok(itemService.findNewItems(findNewItemsCommand));
    }

}
