package com.prgrms.nabmart.global.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.prgrms.nabmart.domain.payment.service.response.TossPaymentApiResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

@ExtendWith(SpringExtension.class)
@RestClientTest(ApiService.class)
class ApiServiceTest {

    @Autowired
    private ApiService apiService;

    @Autowired
    private MockRestServiceServer mockServer;

    @Value("${payment.toss.confirm-url}")
    private String confirmUrl;

    @Nested
    @DisplayName("getResult 메서드는")
    class GetResultTest {

        @Test
        @DisplayName("외부 api 에 요청하여 응답을 받는다")
        void getResponseWithExternalApi() {
            // given
            String mockSecretKey = "mockSecretKey";
            String expectedJsonResponse = "{\"method\": \"카드\",\"status\": \"DONE\"}";
            String encodedSecretKey = new String(Base64.getEncoder()
                .encode((mockSecretKey + ":").getBytes(StandardCharsets.UTF_8)));

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth(encodedSecretKey);
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            JSONObject params = new JSONObject();
            params.put("paymentKey", "mockPaymentKey");
            params.put("orderId", 1);
            params.put("amount", 10000);

            mockServer
                .expect(requestTo(confirmUrl))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Basic " + encodedSecretKey))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(withSuccess(expectedJsonResponse, MediaType.APPLICATION_JSON));

            // when
            TossPaymentApiResponse response = apiService.getResult(
                new HttpEntity<>(params, httpHeaders),
                confirmUrl,
                TossPaymentApiResponse.class);

            // then
            assertThat(response).isNotNull();
            assertThat(response.method()).isEqualTo("카드");
            assertThat(response.status()).isEqualTo("DONE");

            mockServer.verify();
        }
    }
}
