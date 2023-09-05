package com.prgrms.nabmart.domain.payment.controller;

import static com.prgrms.nabmart.domain.order.support.OrderFixture.getPendingOrder;
import static com.prgrms.nabmart.domain.payment.support.PaymentDtoFixture.paymentCommandWithCard;
import static com.prgrms.nabmart.domain.payment.support.PaymentDtoFixture.paymentRequestWithCard;
import static com.prgrms.nabmart.domain.payment.support.PaymentDtoFixture.paymentResponse;
import static com.prgrms.nabmart.domain.user.support.UserFixture.getUser;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.payment.controller.request.PaymentRequest;
import com.prgrms.nabmart.domain.payment.controller.response.PaymentResponse;
import com.prgrms.nabmart.domain.payment.service.request.PaymentCommand;
import com.prgrms.nabmart.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PaymentControllerTest extends BaseControllerTest {

    @Value("${payment.toss.success_url}")
    private String successCallBackUrl;

    @Value("${payment.toss.fail_url}")
    private String failCallBackUrl;

    @Nested
    @DisplayName("pay 메서드 실행 시")
    class payTest {

        @Test
        @DisplayName("성공")
        void postPay() throws Exception {
            // given
            User user = getUser(1);
            Order order = getPendingOrder(1, user);
            PaymentRequest paymentRequest = paymentRequestWithCard();
            PaymentCommand paymentCommand = paymentCommandWithCard();
            PaymentResponse paymentResponse = paymentResponse(order, successCallBackUrl,
                failCallBackUrl);
            Mockito.when(paymentService.pay(order.getOrderId(), paymentCommand))
                .thenReturn(paymentResponse);

            // when
            ResultActions result = mockMvc.perform(
                post("/api/v1/pays/{orderId}", order.getOrderId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paymentRequest)));

            // then
            result
                .andExpect(status().isOk())
                .andDo(document("post-pay",
                    pathParameters(
                        parameterWithName("orderId").description("주문 ID")
                    ),
                    requestFields(
                        fieldWithPath("paymentType").type(STRING).description("결제 타입")
                    ),
                    responseFields(
                        fieldWithPath("amount").type(NUMBER).description("금액"),
                        fieldWithPath("paymentType").type(STRING).description("결제 타입"),
                        fieldWithPath("orderId").type(NUMBER).description("주문 ID"),
                        fieldWithPath("orderName").type(STRING).description("주문 대표명"),
                        fieldWithPath("customerEmail").type(STRING).description("고객 이메일"),
                        fieldWithPath("customerName").type(STRING).description("고객명"),
                        fieldWithPath("successUrl").type(STRING).description("성공 시 콜백 url"),
                        fieldWithPath("failUrl").type(STRING).description("실패 시 콜백 url")
                    )
                ));
        }
    }

}