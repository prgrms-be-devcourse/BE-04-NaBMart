package com.prgrms.nabmart.domain.order.service;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.exception.NotFoundOrderException;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.order.service.response.FindOrderDetailItemResponse;
import com.prgrms.nabmart.domain.order.service.response.FindOrderDetailResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public FindOrderDetailResponse findOrderByIdAndUserId(final Long userId, final Long orderId) {
        Order order = getOrderByOrderIdAndUserId(orderId, userId);
        List<FindOrderDetailItemResponse> orderItems = order.getOrderItems()
            .stream()
            .map(FindOrderDetailItemResponse::from)
            .toList();

        return FindOrderDetailResponse.of(order, orderItems);
    }

    private Order getOrderByOrderIdAndUserId(final Long orderId, final Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
            .orElseThrow(() -> new NotFoundOrderException("order 가 존재하지 않습니다"));
    }
}
