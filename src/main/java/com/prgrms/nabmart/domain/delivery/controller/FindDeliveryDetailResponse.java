package com.prgrms.nabmart.domain.delivery.controller;

import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import com.prgrms.nabmart.domain.order.OrderItem;
import java.time.LocalDateTime;
import java.util.List;

public record FindDeliveryDetailResponse(
    Long deliveryId,
    DeliveryStatus deliveryStatus,
    LocalDateTime arrivedAt,
    String address,
    String orderName,
    int orderPrice,
    String riderRequest,
    int deliveryFee,
    List<OrderItemResponse> items) {


    public static FindDeliveryDetailResponse from(final Delivery delivery) {
        List<OrderItemResponse> items = delivery.getOrder().getOrderItems().stream()
            .map(OrderItemResponse::from)
            .toList();
        return new FindDeliveryDetailResponse(
            delivery.getDeliveryId(),
            delivery.getDeliveryStatus(),
            delivery.getArrivedAt(),
            delivery.getAddress(),
            delivery.getOrder().getName(),
            delivery.getOrderPrice(),
            delivery.getRiderRequest(),
            delivery.getDeliveryFee(),
            items);
    }

    public record OrderItemResponse(String name, int quantity, int price) {

        public static OrderItemResponse from(final OrderItem orderItem) {
            return new OrderItemResponse(
                orderItem.getItem().getName(),
                orderItem.getQuantity(),
                orderItem.getItem().getPrice());
        }
    }
}
