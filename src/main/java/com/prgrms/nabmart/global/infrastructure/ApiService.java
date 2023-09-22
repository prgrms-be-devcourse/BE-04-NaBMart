package com.prgrms.nabmart.global.infrastructure;

import com.prgrms.nabmart.global.exception.ExternalApiException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    private final RestTemplate restTemplate;

    public ApiService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(5))
            .build();
    }

    public <T> T getResult(HttpEntity httpEntity, String url, Class<T> clazz) {
        return getResult(httpEntity, url, clazz, Collections.emptyMap());
    }

    public <T> T getResult(HttpEntity httpEntity, String url, Class<T> clazz,
        Map<String, ?> uriVariables) {
        ResponseEntity<T> response = callExternalApi(url, httpEntity, clazz, uriVariables);
        if (response.getStatusCode().isError()) {
            throw new ExternalApiException("외부 API 호출 과정에서 오류가 발생했습니다");
        }

        return response.getBody();
    }

    private <T> ResponseEntity<T> callExternalApi(
        String url,
        HttpEntity httpEntity,
        Class<T> clazz,
        Map<String, ?> uriVariables) {
        try {
            return restTemplate.postForEntity(url, httpEntity, clazz, uriVariables);
        } catch (Exception exception) {
            throw new ExternalApiException("외부 API 호출 과정에서 오류가 발생했습니다");
        }
    }

}
