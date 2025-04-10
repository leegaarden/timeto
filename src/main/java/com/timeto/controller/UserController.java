package com.timeto.controller;


import com.timeto.apiPayload.ApiResponse;
import com.timeto.dto.user.UserResponse;
import com.timeto.oauth.CustomOAuth2User;
import com.timeto.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "유저 API", description = "유저 관련 API")
public class UserController {

    private final UserService userService;
    @GetMapping
    @Operation(summary = "USER_API_01 : 유저 정보 조회", description = "유저의 정보 및 메뉴를 조회합니다.")
    public ApiResponse<UserResponse.GetUserInfoRes> getUserInfo (
            Authentication authentication
    ) {
        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        UserResponse.GetUserInfoRes response = userService.getUserInfo(userId);

        return ApiResponse.success("유저 정보 조회에 성공했습니다.", response);
    }
}
