package com.prgrms.nabmart.global.auth.oauth.dto;

import com.prgrms.nabmart.domain.user.service.response.AuthUserResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final AuthUserResponse userResponse;
    private final List<GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(
        final AuthUserResponse userResponse,
        final Map<String, Object> attributes) {
        this.userResponse = userResponse;
        this.authorities = userResponse.userRole().getAuthorities().stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return userResponse.providerId();
    }
}
