package com.prgrms.nabmart.domain.item.controller;

import com.prgrms.nabmart.domain.item.service.ItemService;
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

    @GetMapping
    public ResponseEntity<FindItemsResponse> findItemsByMainCategory(
        @RequestParam Long previousItemId, @RequestParam int size, @RequestParam Long main) {

        FindItemsResponse findItemsResponse = itemService.findItemsByMainCategory(previousItemId,
            main, size);
        return ResponseEntity.ok(findItemsResponse);
    }
}
