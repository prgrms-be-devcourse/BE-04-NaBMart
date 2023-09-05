package com.prgrms.nabmart.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.global.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerTest extends BaseControllerTest {

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