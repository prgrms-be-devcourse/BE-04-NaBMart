package com.prgrms.nabmart.global.auth.resolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.prgrms.nabmart.domain.user.service.response.AuthUserResponse;
import com.prgrms.nabmart.global.auth.LoginUser;
import com.prgrms.nabmart.global.auth.jwt.TokenProvider;
import com.prgrms.nabmart.global.fixture.AuthFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

class LoginUserArgumentResolverTest {

    @RestController
    static class TestController {

        @GetMapping("/resolvers")
        public Long resolvers(@LoginUser Long userId) {
            return userId;
        }
    }

    MockMvc mvc;
    TokenProvider tokenProvider;
    AuthUserResponse authUserResponse;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new TestController())
            .setCustomArgumentResolvers(new LoginUserArgumentResolver())
            .build();
        tokenProvider = AuthFixture.tokenProvider();
        authUserResponse = AuthFixture.authUserResponse();

        Authentication authentication = AuthFixture.usernamePasswordAuthenticationToken();
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Nested
    @DisplayName("Controller 파라미터에 LoginUser가 있다면")
    class ResolveArgumentTest {

        @Test
        @DisplayName("성공: authentication 객체에서 userId를 추출하여 인자로 전달")
        void success() throws Exception {
            //given
            String token = tokenProvider.createToken(authUserResponse);

            //when
            ResultActions resultActions = mvc.perform(get("/resolvers")
                .header("Authorization", token));

            //then
            resultActions.andDo(print())
                .andExpect(jsonPath("$").isNumber());
        }
    }
}
