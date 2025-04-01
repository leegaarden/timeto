package com.timeto.config.exception.custom.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TextInputValidator implements ConstraintValidator<ValidTextInput, String> {

    private int maxLength;

    @Override
    public void initialize(ValidTextInput constraintAnnotation) {
        this.maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null 값은 @NotNull로 처리해야 하므로 여기서는 유효하다고 처리
        if (value == null) {
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