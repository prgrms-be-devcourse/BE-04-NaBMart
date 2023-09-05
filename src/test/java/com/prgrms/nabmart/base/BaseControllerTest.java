package com.prgrms.nabmart.base;

import com.prgrms.nabmart.global.fixture.AuthFixture;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseControllerTest {

    String accessToken;

    @BeforeEach
    void setUp() {
        Authentication authentication = AuthFixture.usernamePasswordAuthenticationToken();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        accessToken = AuthFixture.accessToken();
    }
}
