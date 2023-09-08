package com.prgrms.nabmart.domain.item.controller;

import com.prgrms.nabmart.domain.item.service.ItemService;
import com.prgrms.nabmart.domain.item.service.request.FindItemDetailCommand;
import com.prgrms.nabmart.domain.item.service.request.FindItemsByMainCategoryCommand;
import com.prgrms.nabmart.domain.item.service.request.FindNewItemsCommand;
import com.prgrms.nabmart.domain.item.service.response.FindItemDetailResponse;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;
    private final String DEFAULT_PREVIOUS_ID = "-1";

    @GetMapping
    public ResponseEntity<FindItemsResponse> findItemsByMainCategory(
        @RequestParam(defaultValue = DEFAULT_PREVIOUS_ID) Long lastIdx,
        @RequestParam int size,
        @RequestParam String main,
        @RequestParam String sort) {

        FindItemsByMainCategoryCommand findItemsByMainCategoryCommand = FindItemsByMainCategoryCommand.of(
            lastIdx, main, size, sort);
        FindItemsResponse findItemsResponse = itemService.findItemsByMainCategory(
            findItemsByMainCategoryCommand);
        return ResponseEntity.ok(findItemsResponse);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<FindItemDetailResponse> findItemDetail(@PathVariable Long itemId) {
        FindItemDetailCommand findItemDetailCommand = FindItemDetailCommand.from(itemId);
        return ResponseEntity.ok(itemService.findItemDetail(findItemDetailCommand));
    }
    
    @GetMapping("/new")
    public ResponseEntity<FindItemsResponse> findNewItems(
        @RequestParam(defaultValue = DEFAULT_PREVIOUS_ID) Long lastIdx,
        @RequestParam int size,
        @RequestParam(defaultValue = "POPULAR") String sort
    ) {
        FindNewItemsCommand findNewItemsCommand = FindNewItemsCommand.of(lastIdx, size, sort);
        return ResponseEntity.ok(itemService.findNewItems(findNewItemsCommand));
    }
}
    