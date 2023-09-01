package com.prgrms.nabmart.domain.cart.service;

import com.prgrms.nabmart.domain.cart.Cart;
import com.prgrms.nabmart.domain.cart.CartItem;
import com.prgrms.nabmart.domain.cart.repository.CartItemRepository;
import com.prgrms.nabmart.domain.cart.repository.CartRepository;
import com.prgrms.nabmart.domain.cart.service.request.RegisterCartItemCommand;
import com.prgrms.nabmart.domain.item.domain.Item;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long registerCartItem(RegisterCartItemCommand registerCartItemCommand) {
        Cart foundCart = cartRepository.findById(registerCartItemCommand.cartId())
            .orElseThrow(NoSuchElementException::new);
        Item foundItem = itemRepository.findById(registerCartItemCommand.itemId())
            .orElseThrow(NoSuchElementException::new);

        CartItem cartItem = new CartItem(foundCart, foundItem, registerCartItemCommand.quantity());

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        return savedCartItem.getCartItemId();
    }
}
