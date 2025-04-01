package com.timeto.controller;

import com.timeto.apiPayload.ApiResponse;
import com.timeto.dto.goal.GoalRequest;
import com.timeto.dto.goal.GoalResponse;
import com.timeto.oauth.CustomOAuth2User;
import com.timeto.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
@Tag(name = "목표 API", description = "목표 관련 API")
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    @Operation(summary = "GOAL_API_01 : 목표 생성", description = "새로운 목표를 생성합니다.")
    public ApiResponse<GoalResponse.CreateGoalRes> createGoal(
            @Valid @RequestBody GoalRequest.CreateGoalReq request,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 목표 생성
        GoalResponse.CreateGoalRes response = goalService.createGoal(request, userId);

        return ApiResponse.success("목표가 생성되었습니다.", response);
    }

    @GetMapping
    @Operation(summary = "GOAL_API_02 : 사용자의 모든 목표와 폴더 조회", description = "사용자의 모든 목표와 각 목표에 속한 폴더 목록을 조회합니다.")
    public ApiResponse<GoalResponse.GetUserGoalsRes> getUserGoals(Authentication authentication) {
        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 사용자의 모든 목표와 폴더 조회
        GoalResponse.GetUserGoalsRes response = goalService.getUserGoals(userId);

        return ApiResponse.success("모든 목표와 폴더가 조회되었습니다.", response);
    }
}