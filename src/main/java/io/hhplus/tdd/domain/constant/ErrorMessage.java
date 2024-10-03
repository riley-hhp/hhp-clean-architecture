package io.hhplus.tdd.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum ErrorMessage {

    INVALID_DATE("유효하지 않은 날짜입니다."),
    INVALID_USER("유효하지 않은 사용자입니다."),
    INSUFFICIENT_CAPACITY("잔여 좌석이 없습니다."),
    ALREADY_SIGNED_LECTURE("기존 수강 이력이 있습니다."),
    NOT_FOUND_ENTITY("%s을(를) 찾을 수 없습니다."),
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}