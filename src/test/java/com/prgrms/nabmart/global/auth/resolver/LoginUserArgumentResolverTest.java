package com.prgrms.nabmart.global.auth.resolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.domain.user.service.response.RegisterUserResponse;
import com.prgrms.nabmart.global.auth.LoginUser;
import com.prgrms.nabmart.global.auth.exception.UnAuthenticationException;
import com.prgrms.nabmart.global.auth.jwt.TokenProvider;
import com.prgrms.nabmart.global.auth.jwt.dto.CreateTokenCommand;
import com.prgrms.nabmart.global.auth.support.AuthFixture;
import com.prgrms.nabmart.global.util.ErrorTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

class LoginUserArgumentResolverTest {

    @RestController
    static class TestController {

        @GetMapping("/resolvers")
        public Long resolvers(@LoginUser Long userId) {
            return userId;
        }

        @ExceptionHandler(UnAuthenticationException.class)
        public ResponseEntity<ErrorTemplate> ex(UnAuthenticationException ex) {
            ErrorTemplate errorTemplate = ErrorTemplate.of(ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorTemplate);
        }
    }

    MockMvc mvc;
    TokenProvider tokenProvider;
    RegisterUserResponse userResponse;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new TestController())
            .setCustomArgumentResolvers(new LoginUserArgumentResolver())
            .build();
        tokenProvider = AuthFixture.tokenProvider();
        userResponse = AuthFixture.registerUserResponse();
    }

    @Nested
    @DisplayName("Controller 파라미터에 LoginUser가 있다면")
    class ResolveArgumentTest {

        @Test
        @DisplayName("성공: authentication 객체에서 userId를 추출하여 인자로 전달")
        void success() throws Exception {
            //given
            Authentication authentication = AuthFixture.usernamePasswordAuthenticationToken();
            SecurityContextHolder.getContext().setAuthentication(authentication);

            CreateTokenCommand createTokenCommand = CreateTokenCommand.from(userResponse);
            String token = tokenProvider.createToken(createTokenCommand);

            //when
            ResultActions resultActions = mvc.perform(get("/resolvers")
                .header("Authorization", token));

            //then
            resultActions.andDo(print())
                .andExpect(jsonPath("$").isNumber());
        }

        @Test
        @DisplayName("예외: authentication 객체가 존재하지 않음")
        void throwExceptionWhenAuthenticationIsNull() throws Exception {
            //given
            SecurityContextHolder.getContext().setAuthentication(null);

            //when
            ResultActions resultActions = mvc.perform(get("/resolvers"));

            //then
            resultActions.andDo(print())
                .andExpect(status().isUnauthorized());
        }
    }
}
