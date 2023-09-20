package com.prgrms.nabmart.global.auth.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.global.auth.controller.AuthControllerAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

class AuthControllerAdviceTest {

    @RestController
    static class AuthExceptionTestController {

        @GetMapping("/jwt-ex")
        public void authEx() {
            throw new InvalidJwtException("잘못된 토큰");
        }

        @GetMapping("/oauth-unlink-ex")
        public void oauthUnlinkEx() {
            throw new OAuthUnlinkFailureException("소셜 로그인 연동 해제 실패");
        }

        @GetMapping("/un-auth-ex")
        public void unAuthEx() {
            throw new UnAuthenticationException("인증되지 않음");
        }

        @GetMapping("/duplicate-username-ex")
        public void usernameDuplicateEx() {
            throw new DuplicateUsernameException("사용자명 중복");
        }
    }

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new AuthExceptionTestController())
            .setControllerAdvice(new AuthControllerAdvice())
            .build();
    }

    @Nested
    @DisplayName("AuthException 발생 시")
    class AuthExceptionTest {

        @Test
        @DisplayName("성공: invalidJwtException 잡아서 처리")
        void throwInvalidJwtException() throws Exception {
            //given
            //when
            ResultActions resultActions = mvc.perform(get("/jwt-ex"));

            //then
            resultActions.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").isString());
        }

        @Test
        @DisplayName("성공: oAuthUnlinkFailureException 잡아서 처리")
        void throwOAuthUnlinkFailureException() throws Exception {
            //given
            //when
            ResultActions resultActions = mvc.perform(get("/oauth-unlink-ex"));

            //then
            resultActions.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").isString());
        }

        @Test
        @DisplayName("성공: duplicateUsernameException 잡아서 처리")
        void throwDuplicateUsernameException() throws Exception {
            //given
            //when
            ResultActions resultActions = mvc.perform(get("/duplicate-username-ex"));

            //then
            resultActions.andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").isString());
        }
    }
}