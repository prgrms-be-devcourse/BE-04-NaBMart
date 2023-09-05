package com.prgrms.nabmart.global.auth.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

class AuthExceptionHandlerTest {

    @RestController
    static class AuthExceptionTestController {

        @GetMapping("/auth-ex")
        public void authEx() {
            throw new InvalidJwtException("잘못된 토큰");
        }

        @GetMapping("/oauth-unlink-ex")
        public void oauthUnlinkEx() {
            throw new OAuthUnlinkFailureException("소셜 로그인 연동 해제 실패");
        }
    }

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new AuthExceptionTestController())
            .setControllerAdvice(new AuthExceptionHandler())
            .build();
    }

    @Nested
    @DisplayName("AuthException 발생 시")
    class AuthExceptionTest {

        @Test
        @DisplayName("성공: authException 잡아서 처리")
        void throwAuthException() throws Exception {
            //given
            //when
            ResultActions resultActions = mvc.perform(get("/auth-ex"));

            //then
            resultActions.andDo(print())
                .andExpect(status().isBadRequest())
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
    }
}