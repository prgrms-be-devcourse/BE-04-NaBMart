package com.prgrms.nabmart.domain.order.service;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.exception.NotFoundOrderException;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.order.service.response.FindOrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public FindOrderDetailResponse findOrderByIdAndUserId(final Long userId, final Long orderId) {
        final Order order = getOrderByOrderIdAndUserId(orderId, userId);
        return FindOrderDetailResponse.from(order);
    }

    private Order getOrderByOrderIdAndUserId(final Long orderId, final Long userId) {
        return orderRepository.findByOrderIdAndUser_UserId(orderId, userId)
            .orElseThrow(() -> new NotFoundOrderException("order 가 존재하지 않습니다"));
    }
}
