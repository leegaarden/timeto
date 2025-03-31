package com.timeto.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
@Tag(name = "목표 API", description = "목표 관련 API")
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    @Operation(summary = "목표 생성", description = "새로운 목표를 생성합니다.")
    public ResponseEntity<GoalResponse.CreateGoalRes> createGoal(
            @Valid @RequestBody GoalRequest.CreateGoalReq request,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 목표 생성
        GoalResponse.CreateGoalRes response = goalService.createGoal(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}