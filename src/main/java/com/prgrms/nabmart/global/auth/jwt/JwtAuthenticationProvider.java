package com.prgrms.nabmart.global.auth.jwt;

import com.prgrms.nabmart.global.auth.jwt.dto.Claims;
import com.prgrms.nabmart.global.auth.jwt.dto.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {

    private final TokenProvider tokenProvider;

    public Authentication authenticate(final String accessToken) {
        Claims claims = tokenProvider.validateToken(accessToken);
        JwtAuthentication jwtAuthentication = new JwtAuthentication(claims.userId(), accessToken);
        List<GrantedAuthority> authorities = getAuthorities(claims.authorities());
        return new UsernamePasswordAuthenticationToken(jwtAuthentication, accessToken, authorities);
    }

    private List<GrantedAuthority> getAuthorities(final List<String> authorities) {
        return authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}
