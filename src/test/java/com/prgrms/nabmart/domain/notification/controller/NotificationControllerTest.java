package com.prgrms.nabmart.domain.notification.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class NotificationControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("sseConnection API 호출 시")
    class ConnectNotificationTest {

        @Test
        @DisplayName("성공")
        void connectNotification() throws Exception {
            //given
            SseEmitter emitter = new SseEmitter();

            given(notificationService.connectNotification(any())).willReturn(emitter);

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/notifications/connect")
                .header(AUTHORIZATION, accessToken));

            //then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰"),
                        headerWithName("Last-Event-ID").description("마지막 이벤트 ID").optional()
                    )
                ));
        }
    }
}
