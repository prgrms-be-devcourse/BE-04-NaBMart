package com.prgrms.nabmart.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSchedulerService {

    private final OrderService orderService;

    @Async
    @Scheduled(cron = "0 */5 * * * *")
    public void updateOrderStatus() {
        orderService.updateOrderStatus();
    }
}
