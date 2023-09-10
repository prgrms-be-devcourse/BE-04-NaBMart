package com.prgrms.nabmart.domain.order.controller;

import com.prgrms.nabmart.domain.order.controller.request.CreateOrderRequest;
import com.prgrms.nabmart.domain.order.service.OrderService;
import com.prgrms.nabmart.domain.order.service.request.CreateOrdersCommand;
import com.prgrms.nabmart.domain.order.service.response.FindOrderDetailResponse;
import com.prgrms.nabmart.global.auth.LoginUser;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<Void> createOrder(
        @Valid @RequestBody final CreateOrderRequest createOrderRequest,
        @LoginUser final Long userId
    ) {
        CreateOrdersCommand createOrdersCommand = CreateOrdersCommand.of(userId,
            createOrderRequest);
        Long orderId = orderService.createOrder(createOrdersCommand);
        URI location = URI.create("/api/v1/orders" + orderId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<FindOrderDetailResponse> findOrderByIdAndUserId(
        @PathVariable Long orderId,
        @LoginUser Long userId
    ) {
        return ResponseEntity.ok(orderService.findOrderByIdAndUserId(orderId, userId));
    }

}
