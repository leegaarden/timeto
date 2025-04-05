package com.timeto.controller;

import com.timeto.apiPayload.ApiResponse;
import com.timeto.domain.Folder;
import com.timeto.dto.folder.FolderRequest;
import com.timeto.dto.folder.FolderResponse;
import com.timeto.dto.goal.GoalRequest;
import com.timeto.dto.goal.GoalResponse;
import com.timeto.oauth.CustomOAuth2User;
import com.timeto.service.FolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
@Tag(name = "폴더 API", description = "폴더 관련 API")
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    @Operation(summary = "FOLDER_API_01 : 폴더 생성", description = "목표 내에 새로운 폴더를 생성합니다.")
    public ApiResponse<FolderResponse.CreateFolderRes> createFolder(
            @Valid @RequestBody FolderRequest.CreateFolderReq request,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 폴더 생성
        FolderResponse.CreateFolderRes response = folderService.createFolder(request, userId);

        return ApiResponse.success("폴더가 생성되었습니다.", response);
    }

    @GetMapping("/{folderId}")
    @Operation(summary = "FOLDER_API_02 : 폴더 및 내부 할 일 조회", description = "특정 폴더와 그 안의 할 일 목록을 조회합니다.")
    public ApiResponse<FolderResponse.GetFolderRes> getFolder(
            @PathVariable Long folderId,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 폴더 및 할 일 조회
        FolderResponse.GetFolderRes response = folderService.getFolder(folderId, userId);

        return ApiResponse.success("폴더 정보가 조회되었습니다.", response);
    }

    @GetMapping("only-folders/{goalId}")
    @Operation(summary = "FOLDER_API_03 : 목표 내 폴더 조회", description = "특정 목표에 속한 폴더 목록을 조회합니다.")
    public ApiResponse<FolderResponse.GetFolderOnlyRes> getFoldersByGoal(
            @PathVariable Long goalId,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 목표 내 폴더 목록 조회
        FolderResponse.GetFolderOnlyRes response = folderService.getFoldersByGoal(goalId, userId);

        return ApiResponse.success("목표 내 폴더 목록이 조회되었습니다.", response);
    }

    @PatchMapping("/name")
    @Operation(summary = "FOLDER_API_04 : 폴더 이름 변경", description = "폴더의 이름을 변경합니다.")
    public ApiResponse<FolderResponse.EditFolderNameRes> editGFolderName(
            @Valid @RequestBody FolderRequest.EditFolderNameReq request,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 서비스 호출하여 목표 이름 변경
        FolderResponse.EditFolderNameRes response = folderService.editFolderName(request, userId);

        return ApiResponse.success("목표 이름이 변경되었습니다.", response);
    }

    @DeleteMapping("/{folderId}")
    @Operation(summary = "FOLDER_API_05 : 폴더 삭제", description = "폴더와 내부 할 일을 삭제합니다.")
    public ApiResponse<FolderResponse.DeleteFolderRes> deleteFolder (
            @PathVariable Long folderId,
            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        FolderResponse.DeleteFolderRes response = folderService.deleteFolder(folderId, userId);
        return ApiResponse.success("폴더와 내부 할 일이 삭제되었습니다.", response);
    }

}
