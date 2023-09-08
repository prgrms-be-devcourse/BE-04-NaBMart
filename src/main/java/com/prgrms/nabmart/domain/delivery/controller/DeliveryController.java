package com.prgrms.nabmart.domain.delivery.controller;

import com.prgrms.nabmart.domain.delivery.service.DeliveryService;
import com.prgrms.nabmart.domain.delivery.service.request.FindDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.response.FindDeliveryDetailResponse;
import com.prgrms.nabmart.global.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/{orderId}")
    public ResponseEntity<FindDeliveryDetailResponse> findDelivery(
        @PathVariable final Long orderId,
        @LoginUser final Long userId) {
        FindDeliveryCommand findDeliveryCommand = FindDeliveryCommand.of(userId, orderId);
        FindDeliveryDetailResponse findDeliveryDetailResponse
            = deliveryService.findDelivery(findDeliveryCommand);
        return ResponseEntity.ok(findDeliveryDetailResponse);
    }

}