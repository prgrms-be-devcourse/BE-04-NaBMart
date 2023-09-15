package com.prgrms.nabmart.domain.review.repository;

import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.review.Review;
import com.prgrms.nabmart.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByUserOrderByCreatedAt(User user);

    List<Review> findAllByItemOrderByCreatedAt(Item item);

    Long countByItem_ItemId(Long itemId);

    void deleteByUser(User findUser);

    @Query("select avg(r.rate) from Review r where r.item.itemId = :itemId")
    Double findAverageRatingByItemId(@Param("itemId") Long itemId);

}
