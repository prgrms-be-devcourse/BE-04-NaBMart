package com.prgrms.nabmart.domain.event.controller.request;

import jakarta.validation.constraints.NotNull;

public record RegisterEventRequest(
    @NotNull String title,
    @NotNull String description
) {

}
