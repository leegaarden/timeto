package com.timeto.controller;

import com.timeto.apiPayload.ApiResponse;
import com.timeto.dto.timeBlock.TimeBlockRequest;
import com.timeto.dto.timeBlock.TimeBlockResponse;
import com.timeto.auth.oauth.CustomOAuth2User;
import com.timeto.service.TimeBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping("/{date}")
    @Operation(summary = "TIME_BLOCK_API_02 : 타임 블럭 조회", description = "해당 날짜의 타임 블럭을 조회합니다.")
    public ApiResponse<TimeBlockResponse.GetTimeBlockRes> getTimeBlock (
            @PathVariable LocalDate date,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        TimeBlockResponse.GetTimeBlockRes response = timeBlockService.getTimeBlock(date, userId);

        return ApiResponse.success("타임 블럭이 조회됐습니다.", response);
    }

    @PostMapping("/load")
    @Operation(summary = "TIME_BLOCK_API_03 : 타임 블럭에 할 일 불러오기", description = "기존 할 일을 선택하여 타임 블럭에 추가합니다.")
    public ApiResponse<TimeBlockResponse.GetTaskRes> loadTaskToTimeBlock(
            @Valid @RequestBody TimeBlockRequest.LoadTaskToTimeBlockReq request,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 타임 블럭에 할 일 불러오기
        TimeBlockResponse.GetTaskRes response = timeBlockService.loadTaskToTimeBlock(request, userId);

        return ApiResponse.success("할 일이 타임 블럭에 추가되었습니다.", response);
    }

    @DeleteMapping("/{timeBlockId}")
    @Operation(summary = "TIME_BLOCK_API_04 : 타임 블럭 삭제", description = "타임 블럭과 연결된 할 일을 함께 삭제합니다.")
    public ApiResponse<Long> deleteTimeBlock(
            @PathVariable Long timeBlockId,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 타임 블럭 삭제하고 할 일 ID 반환
        Long deletedTaskId = timeBlockService.deleteTimeBlock(timeBlockId, userId);

        return ApiResponse.success("타임 블럭과 할 일이 삭제되었습니다.", deletedTaskId);
    }

    @PutMapping
    @Operation(summary = "TIME_BLOCK_API_05 : 타임 블럭 및 할 일 수정", description = "타임 블럭과 연결된 할 일을 수정합니다.")
    public ApiResponse<TimeBlockResponse.EditTimeBLockRes> editTimeBlock(
            @Valid @RequestBody TimeBlockRequest.EditTimeBlockReq request,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 타임 블럭과 할 일 수정
        TimeBlockResponse.EditTimeBLockRes response = timeBlockService.editTimeBlock(request, userId);

        return ApiResponse.success("타임 블럭과 할 일이 수정되었습니다.", response);
    }

    @PatchMapping("/order")
    @Operation(summary = "TIME_BLOCK_API_06 : 타임 블럭 순서 이동", description = "타임 블럭의 순서를 이동합니다.")
    public ApiResponse<TimeBlockResponse.EditTimeBLockOrderRes> editTimeBLockOrder (
            @Valid @RequestBody TimeBlockRequest.EditTImeBlockOrderReq request,
            Authentication authentication) {
        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 타임 블럭 순서 이동
        TimeBlockResponse.EditTimeBLockOrderRes response = timeBlockService.editTimeBlockOrder(request, userId);

        return ApiResponse.success("타임 블럭 순서가 이동되었습니다.", response);
    }

}