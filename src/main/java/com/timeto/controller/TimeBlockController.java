package com.timeto.controller;

import com.timeto.apiPayload.ApiResponse;
import com.timeto.dto.timeBlock.TimeBlockRequest;
import com.timeto.dto.timeBlock.TimeBlockResponse;
import com.timeto.oauth.CustomOAuth2User;
import com.timeto.service.TimeBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/time-blocks")
@RequiredArgsConstructor
@Tag(name = "타임 블럭 API", description = "타임 블럭 관련 API")
public class TimeBlockController {

    private final TimeBlockService timeBlockService;

    @PostMapping
    @Operation(summary = "TIME_BLOCK_API_01 : 타임 블럭 및 할 일 생성", description = "특정 시간에 타임 블럭과 할 일을 함께 생성합니다.")
    public ApiResponse<TimeBlockResponse.CreateTimeBLockRes> createTimeBlock(
            @Valid @RequestBody TimeBlockRequest.CreateTimeBlockReq request,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 타임 블럭과 할 일 생성
        TimeBlockResponse.CreateTimeBLockRes response = timeBlockService.createTimeBlock(request, userId);

        return ApiResponse.success("타임 블럭과 할 일이 생성되었습니다.", response);
    }
}