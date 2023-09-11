package com.prgrms.nabmart.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.category.repository.SubCategoryRepository;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderItem;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserGrade;
import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.repository.response.UserOrderCount;
import com.prgrms.nabmart.global.config.JpaAuditingConfig;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MainCategoryRepository mainCategoryRepository;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    TestEntityManager em;

    MainCategory mainCategory = CategoryFixture.mainCategory();
    SubCategory subCategory = CategoryFixture.subCategory(mainCategory);

    @BeforeEach
    void init() {
        mainCategoryRepository.save(mainCategory);
        subCategoryRepository.save(subCategory);
    }

    private User createAndSaveUser() {
        User user = User.builder()
            .nickname("이름")
            .email("email@email.com")
            .provider("provider")
            .providerId("providerId")
            .userGrade(UserGrade.NORMAL)
            .userRole(UserRole.ROLE_USER)
            .build();
        userRepository.save(user);
        return user;
    }

    private List<Order> createAndSaveAllOrders(User user, int end) {
        List<Order> orders = new ArrayList<>();
        for (int i = 1; i <= end; i++) {
            Order order = createOrder(user);
            orders.add(order);
        }
        orderRepository.saveAll(orders);
        return orders;
    }

    private Order createOrder(User user) {
        Item item = createAndSaveItem();
        OrderItem orderItem = new OrderItem(item, 1);
        return new Order(user, List.of(orderItem));
    }

    private Item createAndSaveItem() {
        Item item = ItemFixture.item(mainCategory, subCategory);
        itemRepository.save(item);
        return item;
    }

    @Nested
    @DisplayName("getUserOrderCount 메서드 실행 시")
    class GetUserOrderCountTest {

        Pageable pageable = PageRequest.of(0, 5);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusDays(1);
        LocalDateTime end = now.plusDays(1);

        @Test
        @DisplayName("성공: 유저 1, 주문 5")
        void successWhenOneUserWithFiveOrder() {
            //given
            User user = createAndSaveUser();
            List<Order> orders = createAndSaveAllOrders(user, 5);

            //when
            List<UserOrderCount> userOrderCounts
                = userRepository.getUserOrderCount(start, end, pageable);

            //then
            assertThat(userOrderCounts).hasSize(1);
            assertThat(userOrderCounts.get(0).getOrderCount()).isEqualTo(5);
        }

        @Test
        @DisplayName("성공: 유저 2, 주문10-주문5")
        void successWhenTwoUserWithTenAndFiveOrder() {
            //given
            User user1 = createAndSaveUser();
            List<Order> orders1 = createAndSaveAllOrders(user1, 10);
            User user2 = createAndSaveUser();
            List<Order> orders2 = createAndSaveAllOrders(user2, 5);

            //when
            List<UserOrderCount> userOrderCounts
                = userRepository.getUserOrderCount(start, end, pageable);

            //then
            assertThat(userOrderCounts).hasSize(2);
            assertThat(userOrderCounts).map(UserOrderCount::getUserId)
                .containsExactlyInAnyOrder(user1.getUserId(), user2.getUserId());
            assertThat(userOrderCounts).map(UserOrderCount::getOrderCount)
                .containsExactlyInAnyOrder(10, 5);
        }
    }

    @Nested
    @DisplayName("updateUserGrade 메서드 실행 시")
    class UpdateUserGradeTest {

        @Test
        @DisplayName("성공")
        void success() {
            //given
            List<User> users = IntStream.range(0, 5)
                .mapToObj(i -> createAndSaveUser())
                .toList();
            List<Long> userIds = users.stream()
                .map(User::getUserId)
                .toList();
            em.flush();
            em.clear();

            //when
            userRepository.updateUserGrade(UserGrade.VIP, userIds);

            //then
            List<User> findUsers = userRepository.findAll();
            assertThat(findUsers).map(User::getUserGrade).containsOnly(UserGrade.VIP);
        }
    }
}