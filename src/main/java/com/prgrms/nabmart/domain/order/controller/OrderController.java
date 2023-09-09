package com.prgrms.nabmart.domain.order.controller;

import com.prgrms.nabmart.domain.order.service.OrderService;
import com.prgrms.nabmart.domain.order.service.response.FindOrderDetailResponse;
import com.prgrms.nabmart.global.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<FindOrderDetailResponse> findOrderByIdAndUserId(
        @PathVariable Long orderId,
        @LoginUser Long userId
    ) {
        return ResponseEntity.ok(orderService.findOrderByIdAndUserId(orderId, userId));
    }

}
