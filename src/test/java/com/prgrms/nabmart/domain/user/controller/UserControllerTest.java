package com.prgrms.nabmart.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.service.UserService;
import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.global.auth.oauth.client.OAuthRestClient;
import com.prgrms.nabmart.global.fixture.AuthFixture;
import com.prgrms.nabmart.global.fixture.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @MockBean
    OAuthRestClient oAuthRestClient;

    String accessToken;

    @BeforeEach
    void setUp() {
        Authentication authentication = AuthFixture.usernamePasswordAuthenticationToken();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        accessToken = AuthFixture.accessToken();
    }

    @Nested
    @DisplayName("findUser 메서드 실행 시")
    class FindUserTest {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            //given
            FindUserDetailResponse findUserDetailResponse = UserFixture.findUserDetailResponse();

            given(userService.findUser(any())).willReturn(findUserDetailResponse);

            //when
            ResultActions resultActions = mvc.perform(get("/api/v1/users/me")
                .header("Authorization", accessToken));

            //then
            resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("Register User",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
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
        void success() throws Exception {
            //given
            User user = UserFixture.user();
            FindUserDetailResponse findUserDetailResponse = FindUserDetailResponse.from(user);

            given(userService.findUser(any())).willReturn(findUserDetailResponse);

            //when
            ResultActions resultActions = mvc.perform(delete("/api/v1/users"));

            //then
            resultActions.andDo(print())
                .andExpect(status().isNoContent());
        }
    }
}