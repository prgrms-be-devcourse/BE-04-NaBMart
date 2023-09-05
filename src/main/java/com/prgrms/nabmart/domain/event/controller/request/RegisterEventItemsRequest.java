package com.prgrms.nabmart.domain.event.controller.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record RegisterEventItemsRequest(
    @NotEmpty(message = "아이템은 필수 입력 항목입니다.") List<Long> items
) {

}
