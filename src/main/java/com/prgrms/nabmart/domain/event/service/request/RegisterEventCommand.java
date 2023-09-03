package com.prgrms.nabmart.domain.event.service.request;

public record RegisterEventCommand(String title, String description) {

    public static RegisterEventCommand from(final String title, final String description) {
        return new RegisterEventCommand(title, description);
    }
}
