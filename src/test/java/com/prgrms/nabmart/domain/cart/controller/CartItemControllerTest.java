package com.prgrms.nabmart.domain.cart.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.nabmart.domain.cart.controller.request.RegisterCartItemRequest;
import com.prgrms.nabmart.global.fixture.AuthFixture;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class CartItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    }

    @BeforeEach
    void setUp() {
        Authentication authentication = AuthFixture.usernamePasswordAuthenticationToken();
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Nested
    @DisplayName("장바구니 상품 API 실행 시")
    class cartItemTest {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            RegisterCartItemRequest registerCartItemRequest = RegisterCartItemRequest.of(1L, 10);

            // when

            // then
            mockMvc.perform(post("/api/v1/cart-items")
                    .content(objectMapper.writeValueAsString(registerCartItemRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("Create cart-item",
                        requestFields(
                            fieldWithPath("userId").type(JsonFieldType.NUMBER).description("userId"),
                            fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("itemId"),
                            fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("quantity")
                        )
                    )
                );
        }
    }
}
