package com.prgrms.nabmart.global.auth.resolver;

import com.prgrms.nabmart.global.auth.LoginUser;
import com.prgrms.nabmart.global.auth.jwt.dto.JwtAuthentication;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean hasLongParameterType = parameter.getParameterType().isAssignableFrom(Long.class);
        return hasLongParameterType & hasParameterAnnotation;
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication.getPrincipal();
        return jwtAuthentication.userId();
    }
}
