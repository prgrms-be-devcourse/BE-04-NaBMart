package com.prgrms.nabmart.domain.item.controller;

import com.prgrms.nabmart.domain.item.service.ItemService;
import com.prgrms.nabmart.domain.item.service.request.FindItemsByMainCategoryCommand;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final String DEFAULT_ITEM_SORT_TYPE = "POPULAR";

    @GetMapping
    public ResponseEntity<FindItemsResponse> findItemsByMainCategory(
        @RequestParam(defaultValue = DEFAULT_PREVIOUS_ID) Long previousItemId,
        @RequestParam int size,
        @RequestParam String main,
        @RequestParam String sort) {

        FindItemsByMainCategoryCommand findItemsByMainCategoryCommand = FindItemsByMainCategoryCommand.of(
            previousItemId, main, size, sort);
        FindItemsResponse findItemsResponse = itemService.findItemsByMainCategory(
            findItemsByMainCategoryCommand);
        return ResponseEntity.ok(findItemsResponse);
    }
}