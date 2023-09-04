package com.prgrms.nabmart.domain.coupon.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.domain.coupon.service.CouponService;
import com.prgrms.nabmart.domain.coupon.service.request.RegisterCouponCommand;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


class CouponControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CouponService couponService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new CouponController(couponService)).build();
    }

    @Nested
    @DisplayName("쿠폰 생성하는 api 호출 시")
    class CreateCouponApi {

        @Test
        @DisplayName("성공")
        public void success() throws Exception {
            // Given
            String name = "TestName";
            int discount = 10000;
            String description = "TestDescription";
            int minOrderPrice = 1000;
            String endAtString = "2023-12-31";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate endAt = LocalDate.parse(endAtString, formatter);
            RegisterCouponCommand command = new RegisterCouponCommand(name, discount, description,
                minOrderPrice, endAt);

            // When & Then
            when(couponService.createCoupon(command)).thenReturn(1L);
            mockMvc.perform(post("/api/v1/coupons")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{"
                        + "\"name\":\"TestName\","
                        + "\"discount\":10000,"
                        + "\"description\":\"TestDescription\","
                        + "\"minOrderPrice\":1000,"
                        + "\"endAt\":\"2023-12-31\""
                        + "}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/coupons/1"));

            verify(couponService, times(1)).createCoupon(command);
        }
    }
}
