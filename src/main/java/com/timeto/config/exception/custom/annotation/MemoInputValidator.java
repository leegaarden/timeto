package com.timeto.config.exception.custom.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MemoInputValidator implements ConstraintValidator<ValidMemoInput, String> {
    private int maxLength;

    @Override
    public void initialize(ValidMemoInput constraintAnnotation) {
        this.maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null 값이나 빈 문자열은 유효하다고 처리
        if (value == null || value.isEmpty()) {
            return true;
        }

        // 길이 체크
        if (value.length() > maxLength) {
            return false;
        }

        // 특수 문자 체크 (알파벳, 숫자, 한글, 공백만 허용)
        return value.matches("^[가-힣a-zA-Z0-9\\s]+$");
    }
}
