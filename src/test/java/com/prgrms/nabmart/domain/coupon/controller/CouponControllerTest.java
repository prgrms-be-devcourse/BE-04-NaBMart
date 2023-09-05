package com.prgrms.nabmart.domain.coupon.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.nabmart.domain.coupon.service.CouponService;
import com.prgrms.nabmart.domain.coupon.service.request.RegisterCouponCommand;
import com.prgrms.nabmart.domain.coupon.service.request.RegisterUserCouponCommand;
import com.prgrms.nabmart.global.fixture.AuthFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@WebMvcTest(controllers = CouponController.class)
@AutoConfigureMockMvc(addFilters = false)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponService couponService;

    @BeforeEach
    void setUp() {
        Authentication authentication = AuthFixture.usernamePasswordAuthenticationToken();
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Nested
    @DisplayName("쿠폰 생성하는 api 호출 시")
    class CreateCouponApi {

        @Test
        @DisplayName("성공")
        public void success() throws Exception {
            // Given
            RegisterCouponCommand registerCouponCommand = new RegisterCouponCommand("TestName",
                10000,
                "TestDescription", 1000, LocalDate.parse("2023-12-31"));

            // When
            when(couponService.createCoupon(registerCouponCommand)).thenReturn(1L);
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
                            fieldWithPath("name").type(JsonFieldType.STRING).description("couponName"),
                            fieldWithPath("discount").type(JsonFieldType.NUMBER)
                                .description("discount"),
                            fieldWithPath("description").type(JsonFieldType.STRING)
                                .description("couponDescription"),
                            fieldWithPath("minOrderPrice").type(JsonFieldType.NUMBER)
                                .description("minOrderPrice"),
                            fieldWithPath("endAt").type(JsonFieldType.STRING)
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
        public void success() throws Exception {
            // Given
            Long couponId = 1L;
            RegisterUserCouponCommand registerUserCouponCommand = new RegisterUserCouponCommand(1L,
                couponId);

            // When
            when(couponService.registerUserCoupon(registerUserCouponCommand)).thenReturn(1L);
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
                            fieldWithPath("userId").type(JsonFieldType.NUMBER).description("userId"),
                            fieldWithPath("couponId").type(JsonFieldType.NUMBER)
                                .description("couponId"))
                    )
                );
        }
    }
}

