package com.prgrms.nabmart.domain.order.controller;

import com.prgrms.nabmart.domain.order.controller.request.CreateOrderRequest;
import com.prgrms.nabmart.domain.order.controller.request.FindPayedOrdersRequest;
import com.prgrms.nabmart.domain.order.exception.OrderException;
import com.prgrms.nabmart.domain.order.service.response.FindPayedOrdersResponse;
import com.prgrms.nabmart.domain.order.service.OrderService;
import com.prgrms.nabmart.domain.order.service.request.CreateOrdersCommand;
import com.prgrms.nabmart.domain.order.service.request.UpdateOrderByCouponCommand;
import com.prgrms.nabmart.domain.order.service.response.CreateOrderResponse;
import com.prgrms.nabmart.domain.order.service.response.FindOrderDetailResponse;
import com.prgrms.nabmart.domain.order.service.response.FindOrdersResponse;
import com.prgrms.nabmart.domain.order.service.response.UpdateOrderByCouponResponse;
import com.prgrms.nabmart.domain.payment.service.request.FindPayedOrdersCommand;
import com.prgrms.nabmart.global.auth.LoginUser;
import com.prgrms.nabmart.global.util.ErrorTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(
        @Valid @RequestBody final CreateOrderRequest createOrderRequest,
        @LoginUser final Long userId
    ) {
        CreateOrdersCommand createOrdersCommand = CreateOrdersCommand.of(userId,
            createOrderRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(orderService.createOrder(createOrdersCommand));
    }

    @PostMapping("/{orderId}/apply-coupon")
    public ResponseEntity<UpdateOrderByCouponResponse> updateOrderByCoupon(
        @PathVariable final Long orderId,
        @LoginUser final Long userId,
        @RequestParam final Long couponId
    ) {
        UpdateOrderByCouponCommand updateOrderByCouponCommand = UpdateOrderByCouponCommand.of(
            orderId, userId, couponId);

        return ResponseEntity.ok(orderService.updateOrderByCoupon(updateOrderByCouponCommand));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<FindOrderDetailResponse> findOrderByIdAndUserId(
        @PathVariable Long orderId,
        @LoginUser Long userId
    ) {
        return ResponseEntity.ok(orderService.findOrderByIdAndUserId(orderId, userId));
    }

    @GetMapping
    public ResponseEntity<FindOrdersResponse> findOrders(
        @RequestParam(defaultValue = "0") Integer page,
        @LoginUser Long userId
    ) {
        return ResponseEntity.ok(orderService.findOrders(userId, page));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(
        @PathVariable Long orderId,
        @LoginUser Long userId
    ) {
        orderService.deleteOrder(orderId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/payed")
    public ResponseEntity<FindPayedOrdersResponse> findPayedOrders(
        @ModelAttribute @Valid FindPayedOrdersRequest findPayedOrdersRequest,
        @LoginUser Long userId) {
        FindPayedOrdersCommand command = FindPayedOrdersCommand.of(
            userId,
            findPayedOrdersRequest.page());
        FindPayedOrdersResponse orders = orderService.findPayedOrders(command);
        return ResponseEntity.ok(orders);
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorTemplate> handleException(
        final OrderException orderException) {
        log.error(orderException.getMessage());
        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(orderException.getMessage()));
    }
}
