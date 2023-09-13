package com.prgrms.nabmart.domain.item.controller;

import com.prgrms.nabmart.domain.item.controller.request.RegisterItemRequest;
import com.prgrms.nabmart.domain.item.controller.request.UpdateItemRequest;
import com.prgrms.nabmart.domain.item.service.ItemService;
import com.prgrms.nabmart.domain.item.service.request.FindHotItemsCommand;
import com.prgrms.nabmart.domain.item.service.request.FindItemDetailCommand;
import com.prgrms.nabmart.domain.item.service.request.FindItemsByCategoryCommand;
import com.prgrms.nabmart.domain.item.service.request.FindNewItemsCommand;
import com.prgrms.nabmart.domain.item.service.request.RegisterItemCommand;
import com.prgrms.nabmart.domain.item.service.request.UpdateItemCommand;
import com.prgrms.nabmart.domain.item.service.response.FindItemDetailResponse;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private static final String BASE_URI = "/api/v1/items/";

    @GetMapping
    public ResponseEntity<FindItemsResponse> findItemsByCategory(
        @RequestParam(defaultValue = DEFAULT_PREVIOUS_ID) Long lastIdx,
        @RequestParam(defaultValue = DEFAULT_PREVIOUS_ID) Long option,
        @RequestParam int size,
        @RequestParam String main,
        @RequestParam(required = false) String sub,
        @RequestParam String sort) {

        FindItemsByCategoryCommand findItemsByCategoryCommand = FindItemsByCategoryCommand.of(
            lastIdx, option, main, sub, size, sort);
        FindItemsResponse findItemsResponse = itemService.findItemsByCategory(
            findItemsByCategoryCommand);
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
        @RequestParam(defaultValue = DEFAULT_PREVIOUS_ID) Long lastItemId,
        @RequestParam int size,
        @RequestParam(defaultValue = "POPULAR") String sort
    ) {
        FindNewItemsCommand findNewItemsCommand = FindNewItemsCommand.of(lastIdx, lastItemId, size,
            sort);
        return ResponseEntity.ok(itemService.findNewItems(findNewItemsCommand));
    }

    @GetMapping("/hot")
    public ResponseEntity<FindItemsResponse> findHotItems(
        @RequestParam(defaultValue = DEFAULT_PREVIOUS_ID) Long lastIdx,
        @RequestParam(defaultValue = DEFAULT_PREVIOUS_ID) Long lastItemId,
        @RequestParam int size,
        @RequestParam(defaultValue = "POPULAR") String sort
    ) {
        FindHotItemsCommand findHotItemsCommand = FindHotItemsCommand.of(lastIdx, lastItemId, size,
            sort);
        return ResponseEntity.ok(itemService.findHotItems(findHotItemsCommand));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Void> updateItem(
        @PathVariable Long itemId,
        @RequestBody @Valid UpdateItemRequest updateItemRequest
    ) {
        UpdateItemCommand updateItemCommand = UpdateItemCommand.of(itemId, updateItemRequest);
        itemService.updateItem(updateItemCommand);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> saveItem(
        @RequestBody @Valid RegisterItemRequest registerItemRequest
    ) {
        RegisterItemCommand registerItemCommand = RegisterItemCommand.of(
            registerItemRequest.name(),
            registerItemRequest.price(),
            registerItemRequest.description(),
            registerItemRequest.quantity(),
            registerItemRequest.discount(),
            registerItemRequest.maxBuyQuantity(),
            registerItemRequest.mainCategoryId(),
            registerItemRequest.subCategoryId()
        );
        Long savedItemId = itemService.saveItem(registerItemCommand);
        URI location = URI.create(BASE_URI + savedItemId);
        return ResponseEntity.created(location).build();
    }
}
