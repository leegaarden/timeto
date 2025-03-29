package com.timeto.config.exception;

import com.timeto.apiPayload.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import jakarta.validation.ValidationException;


import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {



    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, ValidationException.class})
    public ApiResponse<List<Map<String,String>>> handleValidationException(Exception e) {  // 파라미터 타입을 Exception으로 변경
        log.error("Invalid DTO Value: {}", e.getMessage());
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) e;
            List<Map<String, String>> fieldErrors = validException.getBindingResult().getFieldErrors().stream()
                    .map(fieldError -> Map.of(
                            "field", fieldError.getField(),
                            "message", Objects.requireNonNull(fieldError.getDefaultMessage())
                    ))
                    .toList();
            return ApiResponse.error("Validation failed", ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
        }
        // ConstraintViolationException 처리
        return ApiResponse.error("Validation failed", ErrorCode.INVALID_INPUT_VALUE, List.of(Map.of("message", e.getMessage())));
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Object> handleRuntimeException(RuntimeException e) {
        log.error("Runtime Exception: {}", e.getMessage());

        // GeneralException인 경우
        if (e instanceof GeneralException) {
            GeneralException generalException = (GeneralException) e;
            return ApiResponse.error(generalException.getErrorCode());
        }

        // 그 외의 RuntimeException인 경우
        return ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> handleException(Exception e) {
        log.error("Internal Server Error: {}", e.getMessage());
        return ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
