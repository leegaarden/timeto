package com.timeto.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 서버 에러
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "S001", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S002", "서버 에러가 발생했습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "U001", "존재하지 않는 사용자입니다."),

    // 목표 에러
    DUPLICATE_GOAL_NAME(HttpStatus.BAD_REQUEST, "G001", "목표 이름은 중복될 수 없습니다."),
    GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "G002", "목표를 찾을 수 없습니다."),

    // 폴더 에러
    DUPLICATE_FOLDER_NAME(HttpStatus.BAD_REQUEST, "F001", "같은 목표 내 폴더 이름은 중복될 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
