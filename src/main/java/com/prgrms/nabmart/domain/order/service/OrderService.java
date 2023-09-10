package com.prgrms.nabmart.domain.order.service;

import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.exception.NotFoundItemException;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderItem;
import com.prgrms.nabmart.domain.order.controller.request.CreateOrderRequest.CreateOrderItemRequest;
import com.prgrms.nabmart.domain.order.exception.NotFoundOrderException;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.order.service.request.CreateOrdersCommand;
import com.prgrms.nabmart.domain.order.service.response.FindOrderDetailResponse;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public long createOrder(CreateOrdersCommand createOrdersCommand) {
        User findUser = findUserByUserId(createOrdersCommand.userId());
        List<OrderItem> orderItem = createOrderItem(createOrdersCommand.createOrderRequest()
            .createOrderItemRequests());
        Order order = new Order(findUser, orderItem);

        return orderRepository.save(order).getOrderId();
    }

    @Transactional(readOnly = true)
    public FindOrderDetailResponse findOrderByIdAndUserId(final Long userId, final Long orderId) {
        final Order order = getOrderByOrderIdAndUserId(orderId, userId);
        return FindOrderDetailResponse.from(order);
    }

    private List<OrderItem> createOrderItem(List<CreateOrderItemRequest> orderItemRequests) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CreateOrderItemRequest createOrderRequest : orderItemRequests) {
            Item findItem = findItemByItemId(createOrderRequest.itemId());
            // OrderItem 생성 및 초기화
            OrderItem orderItem = new OrderItem(findItem, createOrderRequest.quantity());
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private Order getOrderByOrderIdAndUserId(final Long orderId, final Long userId) {
        return orderRepository.findByOrderIdAndUser_UserId(orderId, userId)
            .orElseThrow(() -> new NotFoundOrderException("order 가 존재하지 않습니다"));
    }

    private User findUserByUserId(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundUserException("존재하지 않은 사용자입니다."));
    }

    private Item findItemByItemId(Long itemId) {
        return itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundItemException("존재하지 않는 상품입니다."));
    }
}
