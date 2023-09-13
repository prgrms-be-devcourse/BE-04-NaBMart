package com.prgrms.nabmart.domain.order.service;

import com.prgrms.nabmart.domain.coupon.Coupon;
import com.prgrms.nabmart.domain.coupon.UserCoupon;
import com.prgrms.nabmart.domain.coupon.exception.InvalidCouponException;
import com.prgrms.nabmart.domain.coupon.exception.NotFoundUserCouponException;
import com.prgrms.nabmart.domain.coupon.repository.UserCouponRepository;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.exception.InvalidItemException;
import com.prgrms.nabmart.domain.item.exception.NotFoundItemException;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderItem;
import com.prgrms.nabmart.domain.order.OrderStatus;
import com.prgrms.nabmart.domain.order.controller.request.CreateOrderRequest.CreateOrderItemRequest;
import com.prgrms.nabmart.domain.order.exception.NotFoundOrderException;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.order.service.request.CreateOrdersCommand;
import com.prgrms.nabmart.domain.order.service.request.UpdateOrderByCouponCommand;
import com.prgrms.nabmart.domain.order.service.response.CreateOrderResponse;
import com.prgrms.nabmart.domain.order.service.response.FindOrderDetailResponse;
import com.prgrms.nabmart.domain.order.service.response.FindOrdersResponse;
import com.prgrms.nabmart.domain.order.service.response.UpdateOrderByCouponResponse;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private static final Integer PAGE_SIZE = 10;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserCouponRepository userCouponRepository;


    @Transactional
    public CreateOrderResponse createOrder(final CreateOrdersCommand createOrdersCommand) {
        User findUser = findUserByUserId(createOrdersCommand.userId());
        List<OrderItem> orderItem = createOrderItem(createOrdersCommand.createOrderRequest()
            .createOrderItemRequests());
        Order order = new Order(findUser, orderItem);
        orderRepository.save(order).getOrderId();

        return CreateOrderResponse.from(order);
    }

    @Transactional
    public UpdateOrderByCouponResponse updateOrderByCoupon(
        final UpdateOrderByCouponCommand updateOrderByCouponCommand) {
        Order findOrder = getOrderByOrderIdAndUserId(updateOrderByCouponCommand.orderId(),
            updateOrderByCouponCommand.userId());
        UserCoupon findUserCoupon = findUserCouponByIdWithCoupon(
            updateOrderByCouponCommand.couponId());

        validationCoupon(findOrder, findUserCoupon.getCoupon());
        findOrder.setUserCoupon(findUserCoupon);

        return UpdateOrderByCouponResponse.of(findOrder, findUserCoupon.getCoupon());
    }

    @Transactional
    public void updateOrderStatus() {
        //30분
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(30);
        List<OrderStatus> statusList = List.of(OrderStatus.PENDING, OrderStatus.PAYING);

        List<Order> expiredOrders = orderRepository.findByStatusInBeforeExpiredTime(
            expiredTime, statusList);

        for (Order expirdeOrder : expiredOrders) {
            updateItemQuantity(expirdeOrder);
            expirdeOrder.updateOrderStatus(OrderStatus.CANCELED);
        }
    }

    private static void updateItemQuantity(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItem.getItem().increaseQuantity(orderItem.getQuantity());
        }
    }

    @Transactional(readOnly = true)
    public FindOrderDetailResponse findOrderByIdAndUserId(final Long userId, final Long orderId) {
        final Order order = getOrderByOrderIdAndUserId(orderId, userId);
        return FindOrderDetailResponse.from(order);
    }

    @Transactional(readOnly = true)
    public FindOrdersResponse findOrders(final Long userId, final Integer page) {
        final Page<Order> pagination = orderRepository.findByUser_UserId(userId,
            PageRequest.of(page, PAGE_SIZE));

        return FindOrdersResponse.of(pagination.getContent(), pagination.getTotalPages());
    }

    private List<OrderItem> createOrderItem(final List<CreateOrderItemRequest> orderItemRequests) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CreateOrderItemRequest createOrderRequest : orderItemRequests) {
            Item findItem = findItemByItemId(createOrderRequest.itemId());
            Integer quantity = createOrderRequest.quantity();
            validateItemQuantity(findItem, quantity);
            findItem.decreaseQuantity(quantity);
            // OrderItem 생성 및 초기화
            OrderItem orderItem = new OrderItem(findItem, quantity);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private void validateItemQuantity(final Item findItem, final Integer quantity) {
        if (findItem.getQuantity() - quantity < 0) {
            throw new InvalidItemException("상품의 재고 수량이 부족합니다");
        }
    }

    private UserCoupon findUserCouponByIdWithCoupon(Long UserCouponId) {
        return userCouponRepository.findByIdWithCoupon(UserCouponId)
            .orElseThrow(() -> new NotFoundUserCouponException("존재하지 않는 쿠폰입니다"));
    }

    private void validationCoupon(Order order, Coupon coupon) {
        if (order.getPrice() < coupon.getMinOrderPrice()) {
            throw new InvalidCouponException("총 주문 금액이 쿠폰 최소 사용 금액보다 작습니다");
        }
    }

    public Order getOrderByOrderIdAndUserId(final Long orderId, final Long userId) {
        return orderRepository.findByOrderIdAndUser_UserId(orderId, userId)
            .orElseThrow(() -> new NotFoundOrderException("order 가 존재하지 않습니다"));
    }

    private User findUserByUserId(final Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundUserException("존재하지 않은 사용자입니다."));
    }

    private Item findItemByItemId(final Long itemId) {
        return itemRepository.findByItemId(itemId)
            .orElseThrow(() -> new NotFoundItemException("존재하지 않는 상품입니다."));
    }
}
