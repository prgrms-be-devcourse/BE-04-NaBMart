package com.prgrms.nabmart.domain.cart.controller;

import com.prgrms.nabmart.domain.cart.controller.request.RegisterCartItemRequest;
import com.prgrms.nabmart.domain.cart.exception.CartItemException;
import com.prgrms.nabmart.domain.cart.service.CartItemService;
import com.prgrms.nabmart.domain.cart.service.request.RegisterCartItemCommand;
import com.prgrms.nabmart.domain.cart.service.request.UpdateCartItemCommand;
import com.prgrms.nabmart.domain.cart.service.response.FindCartItemsResponse;
import com.prgrms.nabmart.global.auth.LoginUser;
import com.prgrms.nabmart.global.util.ErrorTemplate;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;
    private static final String BASE_URL = "/api/v1/cart-items/";

    @PostMapping
    public ResponseEntity<Void> registerCartItem(
        @Valid @RequestBody RegisterCartItemRequest registerCartItemRequest,
        @LoginUser Long userId
    ) {
        RegisterCartItemCommand command = RegisterCartItemCommand.of(userId,
            registerCartItemRequest.itemId(), registerCartItemRequest.quantity());

        Long cartItemId = cartItemService.registerCartItem(command);

        URI location = URI.create(BASE_URL + cartItemId);

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(
        @PathVariable Long cartItemId
    ) {
        cartItemService.deleteCartItem(cartItemId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartItemId}")
    public ResponseEntity<FindCartItemsResponse> findCartItems(
        @PathVariable Long cartItemId
    ) {
        return ResponseEntity.ok(
            cartItemService.findCartItems(cartItemId)
        );
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<Void> updateCartItemQuantity(
        @PathVariable final Long cartItemId,
        @Valid @RequestBody final int quantity
    ) {
        UpdateCartItemCommand updateCartItemCommand = UpdateCartItemCommand.of(cartItemId,
            quantity);

        cartItemService.updateCartItemQuantity(updateCartItemCommand);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartItemException.class)
    public ResponseEntity<ErrorTemplate> handleException(
        final CartItemException cartItemException) {
        log.info(cartItemException.getMessage());

        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(cartItemException.getMessage()));

    }
}
