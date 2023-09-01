package com.prgrms.nabmart.global.auth.jwt.filter;

import com.prgrms.nabmart.global.auth.jwt.JavaJwtTokenProvider;
import com.prgrms.nabmart.global.auth.jwt.JwtAuthenticationProvider;
import com.prgrms.nabmart.global.auth.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class JwtAuthenticationFilterTest {

    JwtAuthenticationFilter jwtAuthenticationFilter;
    TokenProvider tokenProvider;
    JwtAuthenticationProvider jwtAuthenticationProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new JavaJwtTokenProvider(
                "issuer",
                "thisisnotrandomclientsecretjustforatest",
                60
        );
        jwtAuthenticationProvider = new JwtAuthenticationProvider(tokenProvider);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtAuthenticationProvider);
    }

    @Nested
    @DisplayName("doFilterInternal 메서드 실행 시")
    class DoFilterInternalTest {

        @Test
        @DisplayName("성공")
        void success() throws ServletException, IOException {
            //given
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();
            FilterChain filterChain = mock(FilterChain.class);

            //when
            jwtAuthenticationFilter.doFilter(request, response, filterChain);

            //then
            then(filterChain).should().doFilter(request, response);
        }
    }
}