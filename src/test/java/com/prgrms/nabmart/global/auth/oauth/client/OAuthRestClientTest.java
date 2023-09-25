package com.prgrms.nabmart.global.auth.oauth.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.prgrms.nabmart.domain.user.UserGrade;
import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.global.auth.oauth.OAuthProvider;
import com.prgrms.nabmart.global.auth.oauth.constant.OAuthConstant;
import com.prgrms.nabmart.global.infrastructure.ApiService;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

@ExtendWith(MockitoExtension.class)
class OAuthRestClientTest {

    @InjectMocks
    OAuthRestClient oAuthRestClient;

    @Mock
    ApiService apiService;

    @Mock
    OAuth2AuthorizedClientService authorizedClientService;

    private OAuth2AuthorizedClient oAuth2AuthorizedClient(String provider, int tokenExpiresMillis) {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId(provider)
            .clientId("clientId")
            .clientSecret("clientSecret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT)
            .authorizationGrantType(AuthorizationGrantType.JWT_BEARER)
            .redirectUri("https://redirectURL.com")
            .build();
        Instant now = Instant.now();
        OAuth2AccessToken accessToken
            = new OAuth2AccessToken(
            TokenType.BEARER,
            "accessToken",
            now,
            now.plusMillis(tokenExpiresMillis));
        OAuth2RefreshToken refreshToken
            = new OAuth2RefreshToken("refreshToken", now, now.plusSeconds(60));
        return new OAuth2AuthorizedClient(clientRegistration, "principalName", accessToken,
            refreshToken);
    }

    private FindUserDetailResponse findUserDetailResponse(String provider) {
        return new FindUserDetailResponse(1L, "nickname", "email@email.com", provider,
            "providerId", UserRole.ROLE_USER, UserGrade.NORMAL);
    }

    private Map<String, Object> kakaoTokenRefreshResponse() {
        int expiresIn = Long.valueOf(Instant.now().getEpochSecond()).intValue();

        Map<String, Object> tokenRefreshResponse = new HashMap<>();
        tokenRefreshResponse.put(OAuthConstant.ACCESS_TOKEN, "accessToken");
        tokenRefreshResponse.put(OAuthConstant.EXPIRES_IN, expiresIn);
        tokenRefreshResponse.put(OAuthConstant.REFRESH_TOKEN, "refreshToken");
        return tokenRefreshResponse;
    }

    private Map<String, Object> naverTokenRefreshResponse() {
        String expiresIn = Long.valueOf(Instant.now().getEpochSecond()).toString();

        Map<String, Object> naverResponse = new HashMap<>();
        naverResponse.put(OAuthConstant.ACCESS_TOKEN, "accessToken");
        naverResponse.put(OAuthConstant.EXPIRES_IN, expiresIn);
        naverResponse.put(OAuthConstant.REFRESH_TOKEN, "refreshToken");
        return naverResponse;
    }

    @Nested
    @DisplayName("callUnlinkOAuthUser 메서드 실행 시")
    class CallUnlinkOAuthUserTest {

        @Test
        @DisplayName("성공: 카카오, 인증 서버 액세스 토큰 만료 되지 않음")
        void successKakao() {
            //given
            String kakaoProvider = OAuthProvider.KAKAO.getName();
            OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClient(kakaoProvider,
                1000);
            Map<String, Object> response = new HashMap<>();
            response.put(OAuthConstant.ID, "providerId");
            FindUserDetailResponse userDetailResponse = findUserDetailResponse(kakaoProvider);

            given(authorizedClientService.loadAuthorizedClient(any(), any())).willReturn(
                oAuth2AuthorizedClient);
            given(apiService.getResult(any(), any(), any(), any()))
                .willReturn(response);

            //when
            oAuthRestClient.callUnlinkOAuthUser(userDetailResponse);

            //then
            then(authorizedClientService).should().removeAuthorizedClient(eq(kakaoProvider), any());
            then(authorizedClientService).should(times(0)).saveAuthorizedClient(any(), any());
        }
        @Test
        @Disabled("랜덤하게 실패. 일정 시간 대기시킬 수 있는 도구가 필요.")
        @DisplayName("성공: 카카오, 인증 서버 액세스 토큰 만료됨")
        void successKakaoWhenAccessTokenExpired() {
            //given
            String kakaoProvider = OAuthProvider.KAKAO.getName();
            int tokenExpiredSeconds = 1;
            OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClient(kakaoProvider,
                tokenExpiredSeconds);
            Map<String, Object> unlinkResponse = new HashMap<>();
            unlinkResponse.put(OAuthConstant.ID, "providerId");
            Map<String, Object> tokenRefreshResponse = kakaoTokenRefreshResponse();
            FindUserDetailResponse userDetailResponse = findUserDetailResponse(kakaoProvider);

            given(authorizedClientService.loadAuthorizedClient(any(), any())).willReturn(
                oAuth2AuthorizedClient);
            given(apiService.getResult(any(), any(), any(), any()))
                .willReturn(tokenRefreshResponse)
                .willReturn(unlinkResponse);

            //when
            oAuthRestClient.callUnlinkOAuthUser(userDetailResponse);

            //then
            then(authorizedClientService).should().removeAuthorizedClient(eq(kakaoProvider), any());
            then(authorizedClientService).should().saveAuthorizedClient(any(), any());
        }

        @Test
        @DisplayName("성공: 네이버, 인증 액세스 토큰 만료되지 않음")
        void successNaver() {
            //given
            String naverProvider = OAuthProvider.NAVER.getName();
            OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClient(naverProvider,
                1000);
            Map<String, Object> response = new HashMap<>();
            response.put(OAuthConstant.RESULT, OAuthConstant.SUCCESS);
            FindUserDetailResponse userDetailResponse = findUserDetailResponse(naverProvider);

            given(authorizedClientService.loadAuthorizedClient(any(), any())).willReturn(
                oAuth2AuthorizedClient);
            given(apiService.getResult(any(), any(), any(), any()))
                .willReturn(response);

            //when
            oAuthRestClient.callUnlinkOAuthUser(userDetailResponse);

            //then
            then(authorizedClientService).should().removeAuthorizedClient(eq(naverProvider), any());
            then(authorizedClientService).should(times(0)).saveAuthorizedClient(any(), any());
        }

    }

    @Nested
    @DisplayName("callRefreshAccessToken 메서드 실행 시")
    class CallRefreshAccessTokenTest {

        @Test
        @DisplayName("성공: 카카오")
        void successKakao() {
            //given
            String kakaoProvider = OAuthProvider.KAKAO.getName();
            OAuth2AuthorizedClient authorizedClient = oAuth2AuthorizedClient(kakaoProvider, 1000);
            Map<String, Object> kakaoResponse = kakaoTokenRefreshResponse();
            FindUserDetailResponse userDetailResponse = findUserDetailResponse(kakaoProvider);

            given(authorizedClientService.loadAuthorizedClient(any(), any())).willReturn(
                authorizedClient);
            given(apiService.getResult(any(), any(), any(), any())).willReturn(kakaoResponse);

            //when
            oAuthRestClient.callRefreshAccessToken(userDetailResponse);

            //then
            then(authorizedClientService).should().saveAuthorizedClient(any(), any());
        }
        @Test
        @DisplayName("성공: 네이버")
        void successNaver() {
            //given
            String naverProvider = OAuthProvider.NAVER.getName();
            OAuth2AuthorizedClient authorizedClient = oAuth2AuthorizedClient(naverProvider, 1000);
            Map<String, Object> naverResponse = naverTokenRefreshResponse();
            FindUserDetailResponse userDetailResponse = findUserDetailResponse(naverProvider);

            given(authorizedClientService.loadAuthorizedClient(any(), any())).willReturn(
                authorizedClient);
            given(apiService.getResult(any(), any(), any(), any())).willReturn(naverResponse);

            //when
            oAuthRestClient.callRefreshAccessToken(userDetailResponse);

            //then
            then(authorizedClientService).should().saveAuthorizedClient(any(), any());
        }
    }
}
