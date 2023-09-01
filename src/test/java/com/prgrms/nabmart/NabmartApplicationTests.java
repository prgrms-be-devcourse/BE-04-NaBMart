package com.prgrms.nabmart;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Properties;

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
	}

	@Test
	void contextLoads() {
	}

}
