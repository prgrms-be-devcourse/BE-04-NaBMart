package com.prgrms.nabmart.domain.notification;

import java.text.MessageFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationMessage {
    REGISTER_DELIVERY("주문이 접수되었습니다.",
        "주문하신 {0} 이(가) {1}분 내에 도착할 예정입니다."),
    START_DELIVERY("라이더가 주문을 픽업하였습니다.", "약 {0}분 후에 도착할 예정입니다."),
    COMPLETE_DELIVERY("배달이 완료되었습니다.", "네이B마트를 이용해주셔서 감사합니다.");

    private final String title;
    private final String contentFormat;

    public String getContentFromFormat(Object... arguments) {
        return MessageFormat.format(contentFormat, arguments);
    }
}
