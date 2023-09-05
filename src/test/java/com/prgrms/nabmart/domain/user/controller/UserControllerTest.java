package com.prgrms.nabmart.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("findUser 메서드 실행 시")
    class FindUserTest {

        @Test
        @DisplayName("성공")
        void FindUser() throws Exception {
            //given
            FindUserDetailResponse findUserDetailResponse = UserFixture.findUserDetailResponse();

            given(userService.findUser(any())).willReturn(findUserDetailResponse);

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/users/me")
                .header("Authorization", accessToken)
                .accept(MediaType.APPLICATION_JSON));

            //then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(headerWithName("Authorization").description("액세스 토큰")),
                        responseFields(
                            fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                .description("유저 ID"),
                            fieldWithPath("nickname").type(JsonFieldType.STRING)
                                .description("유저 닉네임"),
                            fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("유저 이메일"),
                            fieldWithPath("provider").type(JsonFieldType.STRING)
                                .description("유저 리소스 서버"),
                            fieldWithPath("providerId").type(JsonFieldType.STRING)
                                .description("유저 리소스 서버 ID"),
                            fieldWithPath("userRole").type(JsonFieldType.STRING)
                                .description("유저 권한"),
                            fieldWithPath("userGrade").type(JsonFieldType.STRING)
                                .description("유저 등급")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("deleteUser 메서드 실행 시")
    class DeleteUserTest {

        @Test
        @DisplayName("성공")
        void DeleteUser() throws Exception {
            //given
            User user = UserFixture.user();
            FindUserDetailResponse findUserDetailResponse = FindUserDetailResponse.from(user);

            given(userService.findUser(any())).willReturn(findUserDetailResponse);

            //when
            ResultActions resultActions = mockMvc.perform(delete("/api/v1/users")
                .header("Authorization", accessToken));

            //then
            resultActions.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                    requestHeaders(
                        headerWithName("Authorization").description("액세스 토큰")
                    )
                ));
        }
    }
}