package com.timeto.config.exception.custom.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MemoInputValidator.class)
public @interface ValidMemoInput {
    // 오류 메시지
    String message() default "입력값은 특수 문자 없이 공백 포함 50자 이내여야 합니다";

    // 유효성 검사 그룹
    Class<?>[] groups() default {};

    // 추가 정보를 위한 payload
    Class<? extends Payload>[] payload() default {};

    // 최대 길이
    int maxLength() default 50;
}
