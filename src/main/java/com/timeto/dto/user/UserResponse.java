package com.timeto.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponse {

    @Schema(title = "USER_RES_01 : 유저 정보 조회 응답")
    public record GetUserInfoRes (
            @Schema(description = "유저 아이디", example = "1")
            Long userId,

            @Schema(description = "유저 이름(닉네임)", example = "탐투")
            String userName,

            @Schema(description = "유저 이메일")
            String userEmail
    ) {}
}
