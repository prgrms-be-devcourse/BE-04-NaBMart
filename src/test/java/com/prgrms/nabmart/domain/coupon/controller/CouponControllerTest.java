package com.prgrms.nabmart.domain.coupon.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.coupon.service.request.RegisterCouponCommand;
import com.prgrms.nabmart.domain.coupon.service.request.RegisterUserCouponCommand;
import com.prgrms.nabmart.domain.coupon.service.response.FindCouponsResponse;
import com.prgrms.nabmart.domain.coupon.support.CouponFixture;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;


@Slf4j
class CouponControllerTest extends BaseControllerTest {


    @Nested
    @DisplayName("쿠폰 생성하는 api 호출 시")
    class CreateCouponApi {

        @Test
        @DisplayName("성공")
        void RegisterCoupon() throws Exception {
            // Given
            RegisterCouponCommand registerCouponCommand = new RegisterCouponCommand("TestName",
                10000,
                "TestDescription", 1000, LocalDate.parse("2023-12-31"));
            when(couponService.createCoupon(registerCouponCommand)).thenReturn(1L);

            // When
            ResultActions resultActions = mockMvc.perform(post("/api/v1/coupons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerCouponCommand)));

            // Then
            resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/coupons/1"))
                .andDo(print())
                .andDo(document("Register coupon",
                        preprocessRequest(
                            prettyPrint()
                        ),
                        requestFields(
                            fieldWithPath("name").type(STRING).description("couponName"),
                            fieldWithPath("discount").type(NUMBER)
                                .description("discount"),
                            fieldWithPath("description").type(STRING)
                                .description("couponDescription"),
                            fieldWithPath("minOrderPrice").type(NUMBER)
                                .description("minOrderPrice"),
                            fieldWithPath("endAt").type(STRING)
                                .description("endAt (yyyy-MM-dd)"))
                    )
                );
        }
    }

    @Nested
    @DisplayName("쿠폰 발급하는 api 호출 시")
    class RegisterUserCouponApi {

        @Test
        @DisplayName("성공")
        void RegisterUserCoupon() throws Exception {
            // Given
            Long couponId = 1L;
            RegisterUserCouponCommand registerUserCouponCommand = new RegisterUserCouponCommand(1L,
                couponId);
            when(couponService.registerUserCoupon(registerUserCouponCommand)).thenReturn(1L);

            // When
            ResultActions resultActions = mockMvc.perform(
                post("/api/v1/my-coupons/{couponId}", couponId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerUserCouponCommand)));

            // Then
            resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/my-coupons/1"))
                .andDo(print())
                .andDo(document("Register userCoupon",
                        preprocessRequest(
                            prettyPrint()
                        ),
                        requestFields(
                            fieldWithPath("userId").type(NUMBER).description("userId"),
                            fieldWithPath("couponId").type(NUMBER)
                                .description("couponId"))
                    )
                );
        }
    }

    @Nested
    @DisplayName("발급가능한 쿠폰 조회 api 호출 시")
    class findCouponsApi {

        @Test
        @DisplayName("성공")
        void findCoupons() throws Exception {
            // Given
            FindCouponsResponse findCouponsResponse = CouponFixture.findCouponsResponse();
            when(couponService.findCoupons()).thenReturn(findCouponsResponse);
            log.info(findCouponsResponse.toString());

            // When
            ResultActions resultActions = mockMvc.perform(get("/api/v1/coupons")
                .contentType(MediaType.APPLICATION_JSON));

            // Then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("coupons").type(ARRAY)
                            .description("발급 가능한 쿠폰 리스트"),
                        fieldWithPath("coupons[].couponId").type(NUMBER)
                            .description("쿠폰 아이디"),
                        fieldWithPath("coupons[].name").type(STRING)
                            .description("쿠폰 이름"),
                        fieldWithPath("coupons[].description").type(STRING)
                            .description("쿠폰 설명"),
                        fieldWithPath("coupons[].discount").type(NUMBER)
                            .description("할인 금액"),
                        fieldWithPath("coupons[].minOrderPrice").type(NUMBER)
                            .description("최소 주문 금액"),
                        fieldWithPath("coupons[].endAt").type(STRING)
                            .description("쿠폰 만료 일자 (yyyy-MM-dd)")
                    )));
        }
    }
}


