package com.prgrms.nabmart.global.config;

import com.prgrms.nabmart.global.auth.jwt.JwtAuthenticationProvider;
import com.prgrms.nabmart.global.auth.jwt.filter.JwtAuthenticationFilter;
import com.prgrms.nabmart.global.auth.oauth.handler.OAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            OAuth2AuthenticationSuccessHandler authenticationSuccessHandler,
            JwtAuthenticationProvider jwtAuthenticationProvider) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
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
                .addFilterAfter(new JwtAuthenticationFilter(jwtAuthenticationProvider), SecurityContextHolderFilter.class);
        return http.build();
    }
}
