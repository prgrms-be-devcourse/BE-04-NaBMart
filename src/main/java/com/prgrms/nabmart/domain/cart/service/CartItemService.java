package com.prgrms.nabmart.domain.cart.service;

import com.prgrms.nabmart.domain.cart.Cart;
import com.prgrms.nabmart.domain.cart.CartItem;
import com.prgrms.nabmart.domain.cart.exception.NotFoundCartItemException;
import com.prgrms.nabmart.domain.cart.repository.CartItemRepository;
import com.prgrms.nabmart.domain.cart.repository.CartRepository;
import com.prgrms.nabmart.domain.cart.service.request.RegisterCartItemCommand;
import com.prgrms.nabmart.domain.cart.service.request.UpdateCartItemCommand;
import com.prgrms.nabmart.domain.item.domain.Item;
import com.prgrms.nabmart.domain.item.exception.NotExistsItemException;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.NotExistUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
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
    public Long registerCartItem(
        RegisterCartItemCommand registerCartItemCommand
    ) {
        User foundUser = userRepository.findById(registerCartItemCommand.userId())
            .orElseThrow(() -> new NotExistUserException("존재하지 않은 사용자입니다."));

        Cart foundCart = cartRepository.findByUser(foundUser)
            .orElseGet(() -> {
                    Cart savedCart = cartRepository.save(new Cart(foundUser));
                    return savedCart;
                }
            );

        Item foundItem = itemRepository.findById(registerCartItemCommand.itemId())
            .orElseThrow(() -> new NotExistsItemException("존재하지 않은 상품입니다."));

        CartItem cartItem = CartItem.builder()
            .cart(foundCart)
            .item(foundItem)
            .quantity(registerCartItemCommand.quantity())
            .build();

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        return savedCartItem.getCartItemId();
    }

    @Transactional
    public void deleteCartItem(
        Long cartItemId
    ) {
        CartItem foundCartItem = findCartItemByCartItemId(cartItemId);

        cartItemRepository.delete(foundCartItem);
    }

    @Transactional
    public void updateCartItemQuantity(UpdateCartItemCommand updateCartItemCommand) {
        CartItem foundCartItem = findCartItemByCartItemId(updateCartItemCommand.cartId());

        foundCartItem.changeQuantity(updateCartItemCommand.quantity());
    }

    private CartItem findCartItemByCartItemId(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
            .orElseThrow(
                () -> new NotFoundCartItemException("장바구니 상품이 존재하지 않습니다.")
            );
    }
}
