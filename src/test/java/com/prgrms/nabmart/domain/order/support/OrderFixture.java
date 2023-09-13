package com.prgrms.nabmart.domain.order.support;

import static com.prgrms.nabmart.domain.coupon.support.CouponFixture.userCoupon;
import static com.prgrms.nabmart.domain.item.support.ItemFixture.item;
import static com.prgrms.nabmart.domain.user.support.UserFixture.user;

import com.prgrms.nabmart.domain.coupon.UserCoupon;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderItem;
import com.prgrms.nabmart.domain.order.OrderStatus;
import com.prgrms.nabmart.domain.order.controller.request.CreateOrderRequest;
import com.prgrms.nabmart.domain.order.controller.request.CreateOrderRequest.CreateOrderItemRequest;
import com.prgrms.nabmart.domain.order.service.request.CreateOrdersCommand;
import com.prgrms.nabmart.domain.order.service.request.UpdateOrderByCouponCommand;
import com.prgrms.nabmart.domain.order.service.response.CreateOrderResponse;
import com.prgrms.nabmart.domain.order.service.response.FindOrderDetailResponse;
import com.prgrms.nabmart.domain.user.User;
import java.util.List;
import org.springframework.test.util.ReflectionTestUtils;

public class OrderFixture {

    public static Order pendingOrder(long orderId, User user) {
        Order order = new Order(user, List.of(orderItem()));
        ReflectionTestUtils.setField(order, "orderId", orderId);

        return order;
    }

    public static Order pendingOrderWithCoupon(long orderId, User user) {
        Order order = new Order(user, List.of(orderItem()));
        ReflectionTestUtils.setField(order, "orderId", orderId);
        ReflectionTestUtils.setField(order, "userCoupon", userCoupon(user));

        return order;
    }

    public static Order payingOrder(long orderId, User user) {
        Order order = new Order(user, List.of(orderItem()));
        ReflectionTestUtils.setField(order, "orderId", orderId);
        ReflectionTestUtils.setField(order, "status", OrderStatus.PAYING);

        return order;
    }

    public static Order deliveringOrder(long orderId, User user) {
        Order order = new Order(user, List.of(orderItem()));
        ReflectionTestUtils.setField(order, "orderId", orderId);
        ReflectionTestUtils.setField(order, "status", OrderStatus.DELIVERING);

        return order;
    }

    public static Order completedOrder(long orderId, User user) {
        Order order = new Order(user, List.of(orderItem()));
        ReflectionTestUtils.setField(order, "orderId", orderId);
        ReflectionTestUtils.setField(order, "status", OrderStatus.COMPLETED);

        return order;
    }

    private static OrderItem orderItem() {
        Item item = item();
        ReflectionTestUtils.setField(item, "itemId", 1L);
        return new OrderItem(item(), 1);
    }

    public static FindOrderDetailResponse orderDetailResponse(Order order) {
        return FindOrderDetailResponse.from(order);
    }

    public static CreateOrderResponse createOrderResponse(Order order) {
        return CreateOrderResponse.from(order);
    }

    public static CreateOrderRequest createOrderRequest() {
        CreateOrderItemRequest orderItemRequest = new CreateOrderItemRequest(1L, 1);
        return new CreateOrderRequest(List.of(orderItemRequest));
    }

    public static CreateOrdersCommand createOrdersCommand() {
        return new CreateOrdersCommand(user().getUserId(), createOrderRequest());
    }

    public static CreateOrdersCommand createOrdersCommand(CreateOrderRequest createOrderRequest) {
        return new CreateOrdersCommand(user().getUserId(), createOrderRequest);
    }

    public static UpdateOrderByCouponCommand updateOrderByCouponCommand(
        Order order, User user, UserCoupon userCoupon) {
        return new UpdateOrderByCouponCommand(order.getOrderId(), user.getUserId(),
            userCoupon.getUserCouponId());
    }
}
