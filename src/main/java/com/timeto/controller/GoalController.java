package com.timeto.controller;

import com.timeto.apiPayload.ApiResponse;
import com.timeto.dto.goal.GoalRequest;
import com.timeto.dto.goal.GoalResponse;
import com.timeto.oauth.CustomOAuth2User;
import com.timeto.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<GoalResponse.GetUserGoalRes> getUserGoals(Authentication authentication) {
        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 사용자의 모든 목표와 폴더 조회
        GoalResponse.GetUserGoalRes response = goalService.getUserGoals(userId);

        return ApiResponse.success("모든 목표와 폴더가 조회되었습니다.", response);
    }

    @GetMapping("/only-goals")
    @Operation(summary = "GOAL_API_03 : 목표만 조회", description = "사용자의 모든 목표와 색상만 조회합니다.")
    public ApiResponse<GoalResponse.GetGoalOnlyRes> getUserGoalsOnly(Authentication authentication) {
        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 사용자의 목표만 조회
        GoalResponse.GetGoalOnlyRes response = goalService.getUserGoalsOnly(userId);

        return ApiResponse.success("목표 목록이 조회되었습니다.", response);
    }

    @PatchMapping("/name")
    @Operation(summary = "GOAL_API_04 : 목표 이름 변경", description = "목표의 이름을 변경합니다.")
    public ApiResponse<GoalResponse.EditGoalNameRes> editGoalName(
            @Valid @RequestBody GoalRequest.EditGoalNameReq request,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 목표 이름 변경
        GoalResponse.EditGoalNameRes response = goalService.editGoalName(request, userId);

        return ApiResponse.success("목표 이름이 변경되었습니다.", response);
    }

    @PatchMapping("/color")
    @Operation(summary = "GOAL_API_05 : 목표 색상 변경", description = "목표의 색상을 변경합니다.")
    public ApiResponse<GoalResponse.EditGoalColorRes> editGoalColor(
            @Valid @RequestBody GoalRequest.EditGoalColorReq request,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 목표 색상 변경
        GoalResponse.EditGoalColorRes response = goalService.editGoalColor(request, userId);

        return ApiResponse.success("목표 색상이 변경되었습니다.", response);
    }

    @DeleteMapping("/{goalId}")
    @Operation(summary = "GOAL_API_06 : 목표 삭제", description = "목표와 그에 속한 모든 폴더 및 할 일을 삭제합니다.")
    public ApiResponse<GoalResponse.DeleteGoalRes> deleteGoal(
            @PathVariable Long goalId,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 목표, 폴더, 할 일 삭제
        GoalResponse.DeleteGoalRes response = goalService.deleteGoal(goalId, userId);

        return ApiResponse.success("목표가 삭제되었습니다.", response);
    }
}