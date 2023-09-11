package com.prgrms.nabmart.domain.order.service;

import static com.prgrms.nabmart.domain.order.support.OrderFixture.completedOrder;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.orderDetailResponse;
import static com.prgrms.nabmart.domain.user.support.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.exception.NotFoundOrderException;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.order.service.response.FindOrderDetailResponse;
import com.prgrms.nabmart.domain.order.service.response.FindOrdersResponse;
import com.prgrms.nabmart.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Nested
    @DisplayName("findOrderByIdAndUserId 메서드 실행 시")
    class FindOrderByIdAndUserId {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            User user = user();
            Order order = completedOrder(1L, user);
            FindOrderDetailResponse expected = orderDetailResponse(order);

            when(orderRepository.findByOrderIdAndUser_UserId(order.getOrderId(), user.getUserId()))
                .thenReturn(Optional.of(order));

            // when
            FindOrderDetailResponse result = orderService
                .findOrderByIdAndUserId(user.getUserId(), order.getOrderId());

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        @DisplayName("예외 : order 가 존재하지 않을 경우, NotFoundOrderException 발생")
        void throwExceptionWhenInvalidOrder() {
            // given
            User user = user();
            Order order = completedOrder(1L, user);

            when(orderRepository.findByOrderIdAndUser_UserId(order.getOrderId(), user.getUserId()))
                .thenReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> orderService.findOrderByIdAndUserId(user.getUserId(), order.getOrderId()));

            // then
            assertThat(exception).isInstanceOf(NotFoundOrderException.class);
        }
    }

    @Nested
    @DisplayName("findOrders 메서드 실행 시")
    class FindOrdersTest {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            User user = user();
            Order order = completedOrder(1L, user);
            Pageable pageable = PageRequest.of(0, 10);
            PageImpl<Order> pagination = new PageImpl<>(List.of(order), pageable, 1L);
            FindOrdersResponse expected = FindOrdersResponse.of(List.of(order), 1);

            when(orderRepository.findByUser_UserId(eq(user.getUserId()), any())).thenReturn(
                pagination);

            // when
            FindOrdersResponse result = orderService.findOrders(user.getUserId(), 0);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }

    }

}
