package com.prgrms.nabmart.base;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.nabmart.domain.cart.service.CartItemService;
import com.prgrms.nabmart.domain.category.service.CategoryService;
import com.prgrms.nabmart.domain.coupon.service.CouponService;
import com.prgrms.nabmart.domain.delivery.service.DeliveryService;
import com.prgrms.nabmart.domain.event.service.EventItemService;
import com.prgrms.nabmart.domain.event.service.EventService;
import com.prgrms.nabmart.domain.item.service.ItemService;
import com.prgrms.nabmart.domain.item.service.LikeItemService;
import com.prgrms.nabmart.domain.notification.service.NotificationService;
import com.prgrms.nabmart.domain.order.service.OrderService;
import com.prgrms.nabmart.domain.payment.service.PaymentClient;
import com.prgrms.nabmart.domain.payment.service.PaymentService;
import com.prgrms.nabmart.domain.review.service.ReviewService;
import com.prgrms.nabmart.domain.user.service.UserService;
import com.prgrms.nabmart.global.auth.jwt.JwtAuthenticationProvider;
import com.prgrms.nabmart.global.auth.jwt.filter.JwtAuthenticationFilter;
import com.prgrms.nabmart.global.auth.oauth.client.OAuthRestClient;
import com.prgrms.nabmart.global.auth.service.RiderAuthenticationService;
import com.prgrms.nabmart.global.auth.support.AuthFixture;
import com.prgrms.nabmart.global.infrastructure.ApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest
@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
public abstract class BaseControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @MockBean
    protected UserService userService;

    @MockBean
    protected CartItemService cartItemService;

    @MockBean
    protected CategoryService categoryService;

    @MockBean
    protected CouponService couponService;

    @MockBean
    protected EventService eventService;

    @MockBean
    protected EventItemService eventItemService;

    @MockBean
    protected OAuthRestClient oAuthRestClient;

    @MockBean
    protected PaymentService paymentService;

    @MockBean
    protected ApiService apiService;

    @MockBean
    protected PaymentClient paymentClient;

    @MockBean
    protected ReviewService reviewService;

    @MockBean
    protected ItemService itemService;

    @MockBean
    protected LikeItemService likeItemService;

    @MockBean
    protected DeliveryService deliveryService;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected RiderAuthenticationService riderAuthenticationService;

    @MockBean
    protected NotificationService notificationService;

    protected static final String AUTHORIZATION = "Authorization";


    protected String accessToken;

    @BeforeEach
    void authenticationSetUp() {
        accessToken = AuthFixture.accessToken();
    }

    @BeforeEach
    void mockMvcSetUp(
        final WebApplicationContext context,
        final RestDocumentationContextProvider provider) {
        JwtAuthenticationProvider jwtAuthenticationProvider
            = new JwtAuthenticationProvider(AuthFixture.tokenProvider());

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .alwaysDo(print())
            .alwaysDo(restDocs)
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .addFilter(new JwtAuthenticationFilter(jwtAuthenticationProvider))
            .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
            .build();
    }

    @Test
    void contextLoads() {

    }
}
