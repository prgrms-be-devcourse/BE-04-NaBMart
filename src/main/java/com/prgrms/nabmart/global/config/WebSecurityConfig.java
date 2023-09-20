package com.prgrms.nabmart.global.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.prgrms.nabmart.global.auth.jwt.JwtAuthenticationProvider;
import com.prgrms.nabmart.global.auth.jwt.filter.JwtAuthenticationFilter;
import com.prgrms.nabmart.global.auth.oauth.handler.OAuth2AuthenticationSuccessHandler;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    //인메모리 방식의 oauth 토큰 저장은 jdbc 방식으로 변경한다.
    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(
        JdbcOperations jdbcOperations,
        ClientRegistrationRepository clientRegistrationRepository) {
        return new JdbcOAuth2AuthorizedClientService(jdbcOperations, clientRegistrationRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        OAuth2AuthenticationSuccessHandler authenticationSuccessHandler,
        JwtAuthenticationProvider jwtAuthenticationProvider) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(requestPermitAll()).permitAll()
                .requestMatchers(requestHasRoleUser()).hasRole("USER")
                .requestMatchers(requestHasRoleRider()).hasRole("RIDER")
                .requestMatchers(requestHasRoleAdmin()).hasRole("ADMIN")
                .anyRequest().denyAll())
            .csrf(AbstractHttpConfigurer::disable)
            .headers(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .rememberMe(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2Login(auth -> auth
                .successHandler(authenticationSuccessHandler))
            .addFilterAfter(new JwtAuthenticationFilter(jwtAuthenticationProvider),
                SecurityContextHolderFilter.class);
        return http.build();
    }

    private RequestMatcher[] requestPermitAll() {
        List<RequestMatcher> requestMatchers = List.of(
            antMatcher(GET, "/api/v1/notifications/**"),
            antMatcher(POST, "/oauth2/authorization/**"),
            antMatcher(POST, "/api/v1/riders/**"),
            antMatcher(GET, "/api/v1/categories/**"),
            antMatcher(GET, "/api/v1/items/**"),
            antMatcher(GET, "/api/v1/events/**"));
        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    private RequestMatcher[] requestHasRoleUser() {
        List<RequestMatcher> requestMatchers = List.of(
            antMatcher("/api/v1/users/me"),
            antMatcher("/api/v1/cart-items/**"),
            antMatcher("/api/v1/my-cart-items"),
            antMatcher("/api/v1/likes/**"),
            antMatcher("/api/v1/reviews/**"),
            antMatcher("/api/v1/users/my-reviews"),
            antMatcher("/api/v1/orders/**"),
            antMatcher("/api/v1/pays/**"),
            antMatcher("/api/v1/coupons/**")
        );
        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    private RequestMatcher[] requestHasRoleRider() {
        List<RequestMatcher> requestMatchers = List.of(
            antMatcher("/api/v1/deliveries/**")
        );
        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    private RequestMatcher[] requestHasRoleAdmin() {
        List<RequestMatcher> requestMatchers = List.of(
            antMatcher("/api/v1/main-categories/**"),
            antMatcher("/api/v1/sub-categories/**"),
            antMatcher("/api/v1/items/**"),
            antMatcher("/api/v1/events/**"),
            antMatcher(POST, "/api/v1/coupons"));
        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
