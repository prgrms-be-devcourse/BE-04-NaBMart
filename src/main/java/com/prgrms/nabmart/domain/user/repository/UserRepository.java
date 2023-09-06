package com.prgrms.nabmart.domain.user.repository;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserGrade;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    @Query("select o.user.userId as userId, count(o) as orderCount from Order o"
        + " where o.createdAt between :start and :end"
        + " group by o.user.userId"
        + " order by o.user.userId asc")
    List<UserOrderCount> getUserOrderCount(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        Pageable pageable);

    @Modifying
    @Query("update User u set u.userGrade = :userGrade"
        + " where u.userId in :userIds")
    void updateUserGrade(
        @Param("userGrade")UserGrade userGrade,
        @Param("userIds") List<Long> userIds);
}
