package com.prgrms.nabmart.domain.event.service.request;

public record RegisterEventCommand(String title, String description) {

    public static RegisterEventCommand of(String title, String description) {
        return new RegisterEventCommand(title, description);
    }
}
