package com.prgrms.nabmart.domain.coupon.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.nabmart.domain.coupon.service.CouponService;
import com.prgrms.nabmart.domain.coupon.service.request.RegisterCouponCommand;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @Nested
    @DisplayName("쿠폰 생성하는 api 호출 시")
    class CreateCouponApi {

        @Test
        @DisplayName("성공")
        public void success() throws Exception {
            // Given
            RegisterCouponCommand command = new RegisterCouponCommand("TestName", 10000,
                "TestDescription", 1000, LocalDate.parse("2023-12-31"));

            // When & Then
            when(couponService.createCoupon(command)).thenReturn(1L);
            mockMvc.perform(post("/api/v1/coupons")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/coupons/1"));

            verify(couponService, times(1)).createCoupon(command);
        }
    }
}
