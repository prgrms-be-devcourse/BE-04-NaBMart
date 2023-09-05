package com.prgrms.nabmart.domain.cart.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.cart.service.request.RegisterCartItemCommand;
import com.prgrms.nabmart.domain.cart.service.response.FindCartItemResponse;
import com.prgrms.nabmart.domain.cart.service.response.FindCartItemsResponse;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class CartItemControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("장바구니 상품 등록 API 실행 시")
    class RegisterCartItemAPITest {

        @Test
        @DisplayName("성공")
        void registerCartItem() throws Exception {

            // given
            RegisterCartItemCommand registerCartItemCommand = RegisterCartItemCommand.of(1L, 1L, 5);

            given(cartItemService.registerCartItem(any())).willReturn(1L);

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/cart-items")
                .content(objectMapper.writeValueAsString(registerCartItemCommand))
                .contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/cart-items/1"))
                .andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                            fieldWithPath("userId").type(JsonFieldType.NUMBER).description("userId"),
                            fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("itemId"),
                            fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("quantity")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("장바구니 상품 삭제 API 실행 시")
    class DeleteCartItemAPITest {

        @Test
        @DisplayName("성공")
        void deleteCartItem() throws Exception {

            // given
            Long cartItemId = 1L;

            // when
            ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/cart-items/{cartItemId}", cartItemId)
                    .contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("cartItemId").description("cartItemId")
                        )
                    )
                );
        }

        @Nested
        @DisplayName("장바구니 상품 수량 수정 API 실행 시")
        class UpdateCartItemQuantityAPITest {

            @Test
            @DisplayName("성공")
            void updateCartItem() throws Exception {

                // given
                Long cartItemId = 1L;
                int updateQuantity = 2;

                // when
                ResultActions resultActions = mockMvc.perform(
                    patch("/api/v1/cart-items/{cartItemId}", cartItemId)
                        .content(objectMapper.writeValueAsString(updateQuantity))
                        .contentType(MediaType.APPLICATION_JSON)
                );

                // then
                resultActions.andExpect(status().isNoContent())
                    .andDo(print())
                    .andDo(
                        restDocs.document(
                            pathParameters(
                                parameterWithName("cartItemId").description("cartItemId")
                            )
                        )
                    );
            }
        }

        @Nested
        @DisplayName("장바구니 상품 목록 조회 API 실행 시")
        class FindCartItemsAPITest {

            @Test
            @DisplayName("성공")
            void findCartItems() throws Exception {
                // given
                Long cartItemId = 1L;
                Long cartId = 1L;
                Long itemId = 1L;
                int quantity = 5;
                FindCartItemsResponse findCartItemsResponse = FindCartItemsResponse.from(
                    Collections.singletonList(new FindCartItemResponse(
                        cartId, itemId, quantity
                    )));

                given(cartItemService.findCartItems(cartItemId)).willReturn(findCartItemsResponse);

                // when
                ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/cart-items/{cartItemId}/list", cartItemId)
                        .contentType(MediaType.APPLICATION_JSON));

                // then
                resultActions.andExpect(status().isOk())
                    .andDo(print())
                    .andDo(restDocs.document(
                        pathParameters(
                            parameterWithName("cartItemId").description("cartItemId")
                        ),
                        responseFields(
                            fieldWithPath("findCartItemsResponse").type(JsonFieldType.ARRAY)
                                .description("List of cart items"),
                            fieldWithPath("findCartItemsResponse[].cartId").type(
                                    JsonFieldType.NUMBER)
                                .description("cartId"),
                            fieldWithPath("findCartItemsResponse[].itemId").type(
                                    JsonFieldType.NUMBER)
                                .description("itemId"),
                            fieldWithPath("findCartItemsResponse[].quantity").type(
                                    JsonFieldType.NUMBER)
                                .description("quantity")
                        )
                    ));
            }
        }
    }
}
