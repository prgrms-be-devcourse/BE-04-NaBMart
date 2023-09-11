package com.prgrms.nabmart.global.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.global.auth.controller.request.RiderLoginRequest;
import com.prgrms.nabmart.global.auth.service.response.RiderLoginResponse;
import com.prgrms.nabmart.global.auth.support.AuthFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class RiderAuthenticationControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("라이더 로그인 API 호출 시")
    class RiderLoginTest {

        @Test
        @DisplayName("성공")
        void riderLogin() throws Exception {
            //given
            RiderLoginRequest riderLoginRequest = AuthFixture.riderLoginRequest();
            RiderLoginResponse riderLoginResponse = new RiderLoginResponse("accessToken");

            given(riderAuthenticationService.riderLogin(any())).willReturn(riderLoginResponse);

            //when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/riders/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(riderLoginRequest))
                .accept(MediaType.APPLICATION_JSON));

            //then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    requestFields(
                        fieldWithPath("username").type(STRING).description("사용자 아이디"),
                        fieldWithPath("password").type(STRING).description("사용자 패스워드")
                    ),
                    responseFields(
                        fieldWithPath("accessToken").type(STRING).description("액세스 토큰")
                    )
                ));
        }
    }
}
