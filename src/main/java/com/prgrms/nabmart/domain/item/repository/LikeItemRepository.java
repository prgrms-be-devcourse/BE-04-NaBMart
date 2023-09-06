package com.prgrms.nabmart.domain.item.repository;

import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.LikeItem;
import com.prgrms.nabmart.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeItemRepository extends JpaRepository<LikeItem, Long> {

    Optional<LikeItem> findByUserAndItem(User user, Item item);
}
