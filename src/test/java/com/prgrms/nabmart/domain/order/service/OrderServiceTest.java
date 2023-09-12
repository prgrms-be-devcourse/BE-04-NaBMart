package com.prgrms.nabmart.domain.order.service;

import static com.prgrms.nabmart.domain.item.support.ItemFixture.item;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.completedOrder;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.createOrderResponse;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.createOrdersCommand;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.orderDetailResponse;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.pendingOrder;
import static com.prgrms.nabmart.domain.user.support.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.exception.InvalidItemException;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.controller.request.CreateOrderRequest;
import com.prgrms.nabmart.domain.order.controller.request.CreateOrderRequest.CreateOrderItemRequest;
import com.prgrms.nabmart.domain.order.exception.NotFoundOrderException;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.order.service.request.CreateOrdersCommand;
import com.prgrms.nabmart.domain.order.service.response.CreateOrderResponse;
import com.prgrms.nabmart.domain.order.service.response.FindOrderDetailResponse;
import com.prgrms.nabmart.domain.order.service.response.FindOrdersResponse;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

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

    @Nested
    @DisplayName("createOrder 메서드 실행 시")
    class CreateOrderTest {

        @Test
        @DisplayName("성공")
        void success() {
            //given
            User user = user();
            Order order = pendingOrder(1L, user);
            ReflectionTestUtils.setField(order, "orderId", null);
            Item item = item();
            CreateOrdersCommand createOrdersCommand = createOrdersCommand();
            CreateOrderResponse expected = createOrderResponse(order);

            when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
            when(itemRepository.findByItemId(any())).thenReturn(Optional.ofNullable(item));
            when(orderRepository.save(any())).thenReturn(order);

            // when
            CreateOrderResponse result = orderService.createOrder(createOrdersCommand);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        @DisplayName("예외 : 상품의 재고가 부족한 경우 , NotInvalidItemException 발생")
        public void throwExceptionWhenInvalidItemQuantity() {
            // Given
            User user = user();
            Item item = item();
            ReflectionTestUtils.setField(item, "itemId", 1L);
            int getQuantity = item.getQuantity();
            int quantityToOrder = getQuantity + 1; // 상품의 재고를 초과하는 수량
            CreateOrderItemRequest orderItemRequest = new CreateOrderItemRequest(1L,
                quantityToOrder);
            CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                List.of(orderItemRequest));
            CreateOrdersCommand createOrdersCommand = createOrdersCommand(createOrderRequest);

            when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
            when(itemRepository.findByItemId(any())).thenReturn(Optional.ofNullable(item));

            // When
            Exception exception = catchException(
                () -> orderService.createOrder(createOrdersCommand));

            // Then
            assertThat(exception).isInstanceOf(InvalidItemException.class)
                .hasMessage("상품의 재고 수량이 부족합니다");
        }
    }
}
