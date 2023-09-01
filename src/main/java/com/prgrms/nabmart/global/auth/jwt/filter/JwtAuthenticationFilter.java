package com.prgrms.nabmart.global.auth.jwt.filter;

import com.prgrms.nabmart.global.auth.jwt.JwtAuthenticationProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final String HEADER = "Authorization";

    private final JwtAuthenticationProvider authenticationProvider;

    @Override
    public void doFilter(
            ServletRequest req,
            ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String accessToken = request.getHeader(HEADER);
        if (Objects.nonNull(accessToken)) {
            Authentication authentication = authenticationProvider.authenticate(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(req, res);
    }
}
