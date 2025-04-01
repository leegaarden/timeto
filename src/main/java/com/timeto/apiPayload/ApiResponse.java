package com.timeto.apiPayload;


import com.timeto.config.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
//@Schema(title = "API 공통 응답")
public class ApiResponse<T> {
    @Schema(description = "상태 코드", example = "200")
    private HttpStatus status;

    @Schema(description = "에러 발생시 에러코드", example = "C401")
    private String code;

    @Schema(description = "응답 메시지")
    private String message;

    @Schema(description = "응답 데이터")
    private T data;

    // 성공 응답 생성 메서드들
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .code(null)
                .message(message)
                .data(data)
                .build();
    }
    // 에러 응답 생성 메서드
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return ApiResponse.<T>builder()
                .status (errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(null)    // 에러 시에는 data를 null로
                .build();
    }
    // 에러 응답 생성 메서드(커스텀 메세지 추가)
    public static <T> ApiResponse<T> error(String message,ErrorCode errorCode,T data) {
        return ApiResponse.<T>builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(message)
                .data(data)
                .build();
    }
}