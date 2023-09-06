package com.prgrms.nabmart.domain.item.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.item.controller.request.RegisterLikeItemRequest;
import com.prgrms.nabmart.global.fixture.ItemFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class LikeControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("찜 아이템 API 호출 시")
    class RegisterLikeItemTest {

        @Test
        @DisplayName("성공")
        void registerLikeItem() throws Exception {
            //given
            RegisterLikeItemRequest registerLikeItemRequest = ItemFixture.registerLikeItemRequest();

            given(likeService.registerLikeItem(any())).willReturn(1L);

            //when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerLikeItemRequest))
                .header(AUTHORIZATION, accessToken));

            //then
            resultActions.andExpect(status().isCreated())
                .andDo(restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
                    requestFields(
                        fieldWithPath("itemId").type(NUMBER).description("아이템 ID")
                    ),
                    responseHeaders(
                        headerWithName("Location").description("리소스 접근 가능 경로")
                    )
                ));
        }
    }
}