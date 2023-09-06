package com.prgrms.nabmart.domain.review.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.review.service.request.RegisterReviewCommand;
import com.prgrms.nabmart.domain.review.support.RegisterReviewCommandFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class ReviewControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("리뷰 등록 API 실행 시")
    class RegisterReviewAPITest {

        @Test
        @DisplayName("성공")
        void registerReview() throws Exception {
            // given
            RegisterReviewCommand registerReviewCommand = RegisterReviewCommandFixture.registerReviewRequest(
                1L, 1L, 5, "내공냠냠"
            );

            given(reviewService.registerReview(any())).willReturn(1L);

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/reviews")
                .content(objectMapper.writeValueAsString(registerReviewCommand))
                .contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/reviews/1"))
                .andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                            fieldWithPath("userId").type(JsonFieldType.NUMBER).description("userId"),
                            fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("itemId"),
                            fieldWithPath("rate").type(JsonFieldType.NUMBER).description("rate"),
                            fieldWithPath("content").type(JsonFieldType.STRING).description("content")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("리뷰 삭제 API 실행 시")
    class DeleteReviewAPITest {

        @Test
        @DisplayName("성공")
        void deleteReview() throws Exception {
            // given
            Long reviewId = 1L;

            // when
            ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/reviews/{reviewId}", reviewId)
                    .contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("reviewId").description("reviewId")
                        )
                    )
                );
        }
    }
}
