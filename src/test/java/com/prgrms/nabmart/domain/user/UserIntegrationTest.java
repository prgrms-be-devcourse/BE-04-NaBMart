package com.prgrms.nabmart.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.nabmart.base.IntegrationTest;
import com.prgrms.nabmart.domain.cart.Cart;
import com.prgrms.nabmart.domain.cart.CartItem;
import com.prgrms.nabmart.domain.cart.support.CartFixture;
import com.prgrms.nabmart.domain.cart.support.CartItemFixture;
import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.coupon.Coupon;
import com.prgrms.nabmart.domain.coupon.UserCoupon;
import com.prgrms.nabmart.domain.coupon.support.CouponFixture;
import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.Rider;
import com.prgrms.nabmart.domain.delivery.support.DeliveryFixture;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.LikeItem;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderItem;
import com.prgrms.nabmart.domain.payment.Payment;
import com.prgrms.nabmart.domain.payment.support.PaymentFixture;
import com.prgrms.nabmart.domain.review.Review;
import com.prgrms.nabmart.domain.review.support.ReviewFixture;
import com.prgrms.nabmart.domain.user.service.UserService;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class UserIntegrationTest extends IntegrationTest {

    @BeforeAll
    static void beforeAll() {
        Properties properties = System.getProperties();
        properties.setProperty("ISSUER", "issuer");
        properties.setProperty("CLIENT_SECRET", "clientSecret");
        properties.setProperty("NAVER_CLIENT_ID", "naverClientId");
        properties.setProperty("NAVER_CLIENT_SECRET", "naverClientSecret");
        properties.setProperty("KAKAO_CLIENT_ID", "kakaoClientId");
        properties.setProperty("KAKAO_CLIENT_SECRET", "kakaoClientSecret");
        properties.setProperty("REDIRECT_URI",
            "http://localhost:8080/login/oauth2/code/{registrationId}");
        properties.setProperty("EXPIRY_SECONDS", "60");
        properties.setProperty("TOSS_SUCCESS_URL", "tossSuccessUrl");
        properties.setProperty("TOSS_FAIL_URL", "tossFailUrl");
        properties.setProperty("TOSS_SECRET_KEY", "tossSecretKey");
        properties.setProperty("REDIS_HOST", "localhost");
        properties.setProperty("REDIS_PORT", "6379");
        properties.setProperty("spring.data.redis.host", "localhost");
        properties.setProperty("spring.data.redis.port", "6379");
    }

    @Autowired
    UserService userService;

    @Autowired
    EntityManager em;

    protected User user = UserFixture.user();
    protected MainCategory mainCategory = CategoryFixture.mainCategory();
    protected SubCategory subCategory = CategoryFixture.subCategory(mainCategory);
    protected Item item = ItemFixture.item(mainCategory, subCategory);
    protected Cart cart = CartFixture.cart(user);
    protected CartItem cartItem = CartItemFixture.cartItem(cart, item, 5);
    protected Review review = ReviewFixture.review(user, item);
    protected LikeItem likeItem = ItemFixture.likeItem(user, item);
    protected Coupon coupon = CouponFixture.coupon();
    protected UserCoupon userCoupon = new UserCoupon(user, coupon);
    protected OrderItem orderItem = new OrderItem(item, 5);
    protected Order order = new Order(user, List.of(orderItem));
    protected Payment payment = PaymentFixture.pendingPayment(user, order);
    protected Rider rider = DeliveryFixture.rider();
    protected Delivery delivery = DeliveryFixture.completedDelivery(order, rider);

    @BeforeEach
    void setUp() {
        userRepository.save(user);
        mainCategoryRepository.save(mainCategory);
        subCategoryRepository.save(subCategory);
        itemRepository.save(item);
        cartRepository.save(cart);
        cartItemRepository.save(cartItem);
        reviewRepository.save(review);
        likeItemRepository.save(likeItem);
        couponRepository.save(coupon);
        userCouponRepository.save(userCoupon);
        orderRepository.save(order);
        paymentRepository.save(payment);
        riderRepository.save(rider);
        deliveryRepository.save(delivery);
    }

    @Nested
    @DisplayName("유저 삭제 시 연과된 엔티티도 삭제된다.")
    class DeleteUserTest {

        @Test
        @DisplayName("성공: 유저와 연관된 데이터는 삭제")
        void successWhenUserDataIsDeleted() {
            //given
            Long userId = user.getUserId();

            //when
            userService.deleteUser(userId);
            em.flush();
            em.clear();

            //then
            assertThat(orderRepository.findById(order.getOrderId())).isEmpty();
            assertThat(orderItemRepository.findById(orderItem.getOrderItemId())).isEmpty();
            assertThat(reviewRepository.findById(review.getReviewId())).isEmpty();
            assertThat(likeItemRepository.findById(likeItem.getLikeItemId())).isEmpty();
            assertThat(cartItemRepository.findById(cartItem.getCartItemId())).isEmpty();
            assertThat(cartRepository.findById(cart.getCartId())).isEmpty();
            assertThat(paymentRepository.findById(payment.getPayId())).isEmpty();
            assertThat(userCouponRepository.findById(userCoupon.getUserCouponId())).isEmpty();
            assertThat(userRepository.findById(user.getUserId())).isEmpty();
        }

        @Test
        @DisplayName("성공: 서비스와 연관된 데이터는 삭제되지 않음")
        void successWhenServiceDataStillAlive() {
            //given
            Long userId = user.getUserId();

            //when
            userService.deleteUser(userId);
            em.flush();
            em.clear();

            //when
            assertThat(
                mainCategoryRepository.findById(mainCategory.getMainCategoryId())).isNotEmpty();
            assertThat(subCategoryRepository.findById(subCategory.getSubCategoryId())).isNotEmpty();
            assertThat(itemRepository.findById(item.getItemId())).isNotEmpty();
            assertThat(couponRepository.findById(coupon.getCouponId())).isNotEmpty();
            assertThat(riderRepository.findById(rider.getRiderId())).isNotEmpty();
            assertThat(deliveryRepository.findById(delivery.getDeliveryId())).isNotEmpty();
        }

        @Test
        @DisplayName("성공: Delivery와 User 관련 필드 업데이트")
        void successDeliveryUpdate() {
            //given
            Long userId = user.getUserId();

            //when
            userService.deleteUser(userId);
            em.flush();
            em.clear();

            //then
            assertThat(delivery.getAddress()).isEqualTo("삭제됨");
            assertThat(delivery.getOrder()).isNull();
        }
    }
}
