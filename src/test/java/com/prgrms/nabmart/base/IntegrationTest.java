package com.prgrms.nabmart.base;

import com.prgrms.nabmart.domain.cart.repository.CartItemRepository;
import com.prgrms.nabmart.domain.cart.repository.CartRepository;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.category.repository.SubCategoryRepository;
import com.prgrms.nabmart.domain.coupon.repository.CouponRepository;
import com.prgrms.nabmart.domain.coupon.repository.UserCouponRepository;
import com.prgrms.nabmart.domain.delivery.repository.DeliveryRepository;
import com.prgrms.nabmart.domain.delivery.repository.RiderRepository;
import com.prgrms.nabmart.domain.event.repository.EventItemRepository;
import com.prgrms.nabmart.domain.event.repository.EventRepository;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.repository.LikeItemRepository;
import com.prgrms.nabmart.domain.order.repository.OrderItemRepository;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.payment.repository.PaymentRepository;
import com.prgrms.nabmart.domain.review.repository.ReviewRepository;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class IntegrationTest {

    @Autowired
    protected CartItemRepository cartItemRepository;

    @Autowired
    protected CartRepository cartRepository;

    @Autowired
    protected MainCategoryRepository mainCategoryRepository;

    @Autowired
    protected SubCategoryRepository subCategoryRepository;

    @Autowired
    protected CouponRepository couponRepository;

    @Autowired
    protected UserCouponRepository userCouponRepository;

    @Autowired
    protected DeliveryRepository deliveryRepository;

    @Autowired
    protected RiderRepository riderRepository;

    @Autowired
    protected EventItemRepository eventItemRepository;

    @Autowired
    protected EventRepository eventRepository;

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected LikeItemRepository likeItemRepository;

    @Autowired
    protected OrderItemRepository orderItemRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected PaymentRepository paymentRepository;

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected UserRepository userRepository;

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
}
