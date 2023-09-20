package com.prgrms.nabmart.domain.notification;

import com.prgrms.nabmart.domain.notification.service.NotificationService;
import com.prgrms.nabmart.global.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/connect", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> sseConnection(
        @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
        @LoginUser Long userId) {

        SseEmitter sseEmitter = notificationService.connection(userId, lastEventId);
        return ResponseEntity.ok(sseEmitter);
    }
}
