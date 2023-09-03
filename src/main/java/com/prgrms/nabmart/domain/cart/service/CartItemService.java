package com.prgrms.nabmart.domain.cart.service;

import com.prgrms.nabmart.domain.cart.Cart;
import com.prgrms.nabmart.domain.cart.CartItem;
import com.prgrms.nabmart.domain.cart.repository.CartItemRepository;
import com.prgrms.nabmart.domain.cart.repository.CartRepository;
import com.prgrms.nabmart.domain.cart.service.request.RegisterCartItemCommand;
import com.prgrms.nabmart.domain.item.domain.Item;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public Long registerCartItem(RegisterCartItemCommand registerCartItemCommand) {

        // TODO : UserException 확인 후 없으면 Exception 생성
        User foundUser = userRepository.findById(registerCartItemCommand.userId())
            .orElseThrow(NoSuchElementException::new);

        Cart foundCart = cartRepository.findByUser(foundUser)
            .orElseGet(() -> {
                    Cart savedCart = cartRepository.save(new Cart(foundUser));
                    return savedCart;
                }
            );

        // TODO : ItemException 확인 후 없으면 Exception 생성
        Item foundItem = itemRepository.findById(registerCartItemCommand.itemId())
            .orElseThrow(NoSuchElementException::new);

        CartItem cartItem = CartItem.builder()
            .cart(foundCart)
            .item(foundItem)
            .quantity(registerCartItemCommand.quantity())
            .build();

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        return savedCartItem.getCartItemId();
    }
}
