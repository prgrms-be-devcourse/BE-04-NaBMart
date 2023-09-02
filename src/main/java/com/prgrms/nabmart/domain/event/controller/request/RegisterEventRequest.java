package com.prgrms.nabmart.domain.event.controller.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterEventRequest(
    @NotBlank(message = "이벤트 제목은 필수 입력 항목입니다.") String title,
    @NotBlank(message = "이벤트 설명은 필수 입력 항목입니다.") String description
) {

}
