package com.prgrms.nabmart.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @MockBean
    OAuthRestClient oAuthRestClient;

    @BeforeEach
    void setUp() {
        Authentication authentication = AuthFixture.usernamePasswordAuthenticationToken();
        SecurityContextHolder.getContext().setAuthentication(authentication);
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