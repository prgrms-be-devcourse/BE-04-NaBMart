package com.prgrms.nabmart;

import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NabmartApplicationTests {

    @BeforeAll
    static void beforeAll() {
        Properties properties = System.getProperties();
        properties.setProperty("ISSUER", "issuer");
        properties.setProperty("CLIENT_SECRET", "clientSecret");
        properties.setProperty("NAVER_CLIENT_ID", "naverClientId");
        properties.setProperty("NAVER_CLIENT_SECRET", "naverClientSecret");
        properties.setProperty("KAKAO_CLIENT_ID", "kakaoClientId");
        properties.setProperty("KAKAO_CLIENT_SECRET", "kakaoClientSecret");
        properties.setProperty("REDIRECT_URI",
            "http://localhost:8080/login/oauth2/code/{registrationId}");
        properties.setProperty("EXPIRY_SECONDS", "60");
        properties.setProperty("TOSS_SUCCESS_URL", "tossSuccessUrl");
        properties.setProperty("TOSS_FAIL_URL", "tossFailUrl");
        properties.setProperty("TOSS_SECRET_KEY", "tossSecretKey");
        properties.setProperty("REDIS_HOST", "localhost");
        properties.setProperty("REDIS_PORT", "6379");
    }

    @Test
    void contextLoads() {
    }

}
