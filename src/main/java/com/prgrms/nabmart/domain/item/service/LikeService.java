package com.prgrms.nabmart.domain.item.service;

import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.LikeItem;
import com.prgrms.nabmart.domain.item.exception.DuplicateLikeException;
import com.prgrms.nabmart.domain.item.exception.NotFoundItemException;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.repository.LikeItemRepository;
import com.prgrms.nabmart.domain.item.service.request.RegisterLikeItemCommand;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final LikeItemRepository likeItemRepository;

    @Transactional
    public Long registerLikeItem(RegisterLikeItemCommand registerLikeItemCommand) {
        User user = findUserByUserId(registerLikeItemCommand.userId());
        Item item = findItemByItemId(registerLikeItemCommand.itemId());
        checkDuplicateLikedItem(user, item);
        LikeItem likeItem = new LikeItem(user, item);
        likeItemRepository.save(likeItem);
        return likeItem.getLikeItemId();
    }

    private void checkDuplicateLikedItem(User user, Item item) {
        likeItemRepository.findByUserAndItem(user, item)
            .ifPresent(likeItem -> {
                throw new DuplicateLikeException("이미 찜한 상품입니다.");
            });
    }

    private User findUserByUserId(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundUserException("존재하지 않는 유저입니다."));
    }

    private Item findItemByItemId(Long itemId) {
        return itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundItemException("존재하지 않는 상품입니다."));
    }

}
