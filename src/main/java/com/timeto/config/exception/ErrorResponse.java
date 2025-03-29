package com.timeto.config.exception;

public record ErrorResponse(int status,String code, String message) {
}
