package com.prgrms.nabmart.domain.delivery.service;

import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.Rider;
import com.prgrms.nabmart.domain.delivery.exception.NotFoundDeliveryException;
import com.prgrms.nabmart.domain.delivery.exception.NotFoundRiderException;
import com.prgrms.nabmart.domain.delivery.repository.DeliveryRepository;
import com.prgrms.nabmart.domain.delivery.repository.RiderRepository;
import com.prgrms.nabmart.domain.delivery.service.request.AcceptDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.CompleteDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.FindDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.FindRiderDeliveriesCommand;
import com.prgrms.nabmart.domain.delivery.service.request.FindWaitingDeliveriesCommand;
import com.prgrms.nabmart.domain.delivery.service.request.StartDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.response.FindDeliveryDetailResponse;
import com.prgrms.nabmart.domain.delivery.service.response.FindRiderDeliveriesResponse;
import com.prgrms.nabmart.domain.delivery.service.response.FindWaitingDeliveriesResponse;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.global.auth.exception.AuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final RiderRepository riderRepository;

    @Transactional(readOnly = true)
    public FindDeliveryDetailResponse findDelivery(FindDeliveryCommand findDeliveryCommand) {
        User user = findUserByUserId(findDeliveryCommand.userId());
        Delivery delivery = findDeliveryByOrderWithOrder(findDeliveryCommand.orderId());
        checkAuthority(delivery, user);
        return FindDeliveryDetailResponse.from(delivery);
    }

    private void checkAuthority(final Delivery delivery, final User user) {
        if (!delivery.isOwnByUser(user)) {
            throw new AuthorizationException("권한이 없습니다.");
        }
    }

    @Transactional
    public void acceptDelivery(AcceptDeliveryCommand acceptDeliveryCommand) {
        Rider rider = findRiderByRiderId(acceptDeliveryCommand.riderId());
        Delivery delivery = findDeliveryByDeliveryId(acceptDeliveryCommand.deliveryId());
        delivery.assignRider(rider);
    }

    @Transactional
    public void startDelivery(StartDeliveryCommand startDeliveryCommand) {
        Rider rider = findRiderByRiderId(startDeliveryCommand.riderId());
        Delivery delivery = findDeliveryByDeliveryId(startDeliveryCommand.deliveryId());
        delivery.checkAuthority(rider);
        delivery.startDelivery(startDeliveryCommand.deliveryEstimateMinutes());
    }

    @Transactional
    public void completeDelivery(CompleteDeliveryCommand completeDeliveryCommand) {
        Rider rider = findRiderByRiderId(completeDeliveryCommand.riderId());
        Delivery delivery = findDeliveryByDeliveryId(completeDeliveryCommand.deliveryId());
        delivery.checkAuthority(rider);
        delivery.completeDelivery();
    }

    private Rider findRiderByRiderId(final Long riderId) {
        return riderRepository.findById(riderId)
            .orElseThrow(() -> new NotFoundRiderException("존재하지 않는 라이더입니다."));
    }

    @Transactional(readOnly = true)
    public FindWaitingDeliveriesResponse findWaitingDeliveries(
        FindWaitingDeliveriesCommand findWaitingDeliveriesCommand) {
        Page<Delivery> deliveriesPage
            = deliveryRepository.findWaitingDeliveries(findWaitingDeliveriesCommand.pageable());
        return FindWaitingDeliveriesResponse.from(deliveriesPage);
    }

    @Transactional(readOnly = true)
    public FindRiderDeliveriesResponse findRiderDeliveries(
        FindRiderDeliveriesCommand findRiderDeliveriesCommand) {
        Rider rider = findRiderByRiderId(findRiderDeliveriesCommand.riderId());
        Page<Delivery> deliveriesPage = deliveryRepository.findRiderDeliveries(
            rider,
            findRiderDeliveriesCommand.deliveryStatuses(),
            findRiderDeliveriesCommand.pageable());
        return FindRiderDeliveriesResponse.of(
            deliveriesPage.getContent(),
            deliveriesPage.getNumber(),
            deliveriesPage.getTotalElements());
    }

    private User findUserByUserId(final Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundUserException("존재하지 않는 유저입니다."));
    }

    private Delivery findDeliveryByOrderWithOrder(final Long orderId) {
        return deliveryRepository.findByOrderIdWithOrder(orderId)
            .orElseThrow(() -> new NotFoundDeliveryException("존재하지 않는 배달입니다."));
    }

    private Delivery findDeliveryByDeliveryId(final Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new NotFoundDeliveryException("존재하지 않는 배달입니다."));
    }
}
