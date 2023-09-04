package com.prgrms.nabmart.global.auth.oauth.dto;

import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;

public record OAuthHttpMessage(
    String uri,
    HttpEntity<MultiValueMap<String, String>> httpMessage,
    Map<String, String> uriVariables) {

}
