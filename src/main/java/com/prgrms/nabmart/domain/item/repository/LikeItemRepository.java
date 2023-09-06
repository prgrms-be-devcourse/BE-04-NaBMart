package com.prgrms.nabmart.domain.item.repository;

import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.LikeItem;
import com.prgrms.nabmart.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeItemRepository extends JpaRepository<LikeItem, Long> {

    Optional<LikeItem> findByUserAndItem(User user, Item item);

    @Query("select li from LikeItem li"
        + " join fetch li.user"
        + " where li.likeItemId = :likeItemId")
    Optional<LikeItem> findByIdWithUser(@Param("likeItemId") Long likeItemId);
}
