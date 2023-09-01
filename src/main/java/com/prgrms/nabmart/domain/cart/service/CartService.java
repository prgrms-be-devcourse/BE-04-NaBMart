package com.prgrms.nabmart.domain.cart.service;

import com.prgrms.nabmart.domain.cart.Cart;
import com.prgrms.nabmart.domain.cart.repository.CartRepository;
import com.prgrms.nabmart.domain.cart.service.request.RegisterCartCommand;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long registerCart(final RegisterCartCommand command) {
        User foundUser = userRepository.findById(command.userId())
            .orElseThrow(EntityNotFoundException::new);

        Cart savedCart = cartRepository.save(new Cart(foundUser));

        return savedCart.getCartId();
    }
}