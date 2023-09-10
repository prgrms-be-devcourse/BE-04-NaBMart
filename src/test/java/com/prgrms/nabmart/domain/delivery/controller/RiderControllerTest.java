package com.prgrms.nabmart.domain.delivery.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.delivery.controller.request.RiderSignupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class RiderControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("라이더 가입 API 호출 시")
    class RiderSignupTest {

        @Test
        @DisplayName("성공")
        void riderSignup() throws Exception {
            //given
            String username = "username";
            String password = "password123";
            String address = "address";
            RiderSignupRequest riderSignupRequest
                = new RiderSignupRequest(username, password, address);
            Long riderId = 1L;

            given(riderService.riderSignup(any())).willReturn(riderId);

            //when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/riders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(riderSignupRequest)));

            //then
            resultActions.andExpect(status().isCreated())
                .andDo(restDocs.document(
                    requestFields(
                        fieldWithPath("username").type(STRING).description("사용자 이름"),
                        fieldWithPath("password").type(STRING).description("패스워드"),
                        fieldWithPath("address").type(STRING).description("배달 선호 주소")
                    ),
                    responseHeaders(
                        headerWithName("Location").description("생성된 리소스 접근 경로")
                    )
                ));
        }
    }
}