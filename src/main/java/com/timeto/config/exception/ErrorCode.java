package com.timeto.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 서버 에러
    INVALID_INPUT_VALUE(400, "S001", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(500, "S002", "서버 에러가 발생했습니다.");

    private final int status;
    private final String code;
    private final String message;
}
