package com.prgrms.nabmart.domain.user.service;

import com.prgrms.nabmart.domain.cart.repository.CartItemRepository;
import com.prgrms.nabmart.domain.cart.repository.CartRepository;
import com.prgrms.nabmart.domain.coupon.repository.UserCouponRepository;
import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.repository.DeliveryRepository;
import com.prgrms.nabmart.domain.item.repository.LikeItemRepository;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.payment.repository.PaymentRepository;
import com.prgrms.nabmart.domain.review.repository.ReviewRepository;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.service.request.FindUserCommand;
import com.prgrms.nabmart.domain.user.service.request.RegisterUserCommand;
import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.domain.user.service.response.RegisterUserResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final LikeItemRepository likeItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserCouponRepository userCouponRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public RegisterUserResponse getOrRegisterUser(final RegisterUserCommand registerUserCommand) {

        User findUser = userRepository.findByProviderAndProviderId(
                registerUserCommand.provider(),
                registerUserCommand.providerId())
            .orElseGet(() -> {
                User user = User.builder()
                    .nickname(registerUserCommand.nickname())
                    .email(registerUserCommand.email())
                    .provider(registerUserCommand.provider())
                    .providerId(registerUserCommand.providerId())
                    .userRole(registerUserCommand.userRole())
                    .userGrade(registerUserCommand.userGrade())
                    .build();
                userRepository.save(user);
                return user;
            });

        return RegisterUserResponse.from(findUser);
    }

    @Transactional(readOnly = true)
    public FindUserDetailResponse findUser(FindUserCommand findUserCommand) {
        User findUser = findUserByUserId(findUserCommand.userId());
        return FindUserDetailResponse.from(findUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User findUser = findUserByUserId(userId);
        releaseDeliveryAboutUserInfo(findUser);
        reviewRepository.deleteByUser(findUser);
        likeItemRepository.deleteByUser(findUser);
        cartItemRepository.deleteByUser(findUser);
        cartRepository.deleteByUser(findUser);
        deliveryRepository.deleteByUser(findUser);
        paymentRepository.deleteByUser(findUser);
        orderRepository.deleteByUser(findUser);
        userCouponRepository.deleteByUser(findUser);
        userRepository.delete(findUser);
    }

    private void releaseDeliveryAboutUserInfo(User findUser) {
        List<Delivery> userDeliveries = deliveryRepository.findAllByUser(findUser);
        userDeliveries.forEach(Delivery::deleteAboutUser);
    }

    private User findUserByUserId(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundUserException("존재하지 않는 유저입니다."));
    }
}
