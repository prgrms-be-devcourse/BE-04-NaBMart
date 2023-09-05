package com.prgrms.nabmart.global.auth.oauth.handler;

import com.prgrms.nabmart.global.auth.exception.InvalidProviderException;
import com.prgrms.nabmart.global.auth.oauth.client.KakaoMessageProvider;
import com.prgrms.nabmart.global.auth.oauth.client.NaverMessageProvider;
import com.prgrms.nabmart.global.auth.oauth.client.OAuthHttpMessageProvider;
import com.prgrms.nabmart.global.auth.oauth.dto.OAuthUserInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum OAuthProvider {
    KAKAO("kakao", attributes -> {
        Map<String, String> properties = (Map<String, String>) attributes.get("properties");
        String oAuthUserId = String.valueOf(attributes.get("id"));
        String nickname = properties.get("nickname");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = String.valueOf(kakaoAccount.get("email"));
        return new OAuthUserInfo(oAuthUserId, nickname, email);
    }, new KakaoMessageProvider()),
    NAVER("naver", attributes -> {
        Map<String, String> response = (Map<String, String>) attributes.get("response");
        String oAuthUserId = response.get("id");
        String nickname = response.get("nickname");
        String email = response.get("email");
        return new OAuthUserInfo(oAuthUserId, nickname, email);
    }, new NaverMessageProvider());

    private static final Map<String, OAuthProvider> PROVIDERS =
        Collections.unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(OAuthProvider::getName, Function.identity())));

    private final String name;
    private final Function<Map<String, Object>, OAuthUserInfo> extractUserInfo;
    private final OAuthHttpMessageProvider oAuthHttpMessageProvider;

    public static OAuthProvider getOAuthProvider(final String provider) {
        OAuthProvider oAuthProvider = PROVIDERS.get(provider);
        return Optional.ofNullable(oAuthProvider)
            .orElseThrow(() -> new InvalidProviderException("지원하지 않는 소셜 로그인입니다."));
    }

    public OAuthUserInfo getOAuthUserInfo(final Map<String, Object> attributes) {
        return this.extractUserInfo.apply(attributes);
    }
}