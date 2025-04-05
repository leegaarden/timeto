package com.timeto.dto.folder;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class FolderResponse {

    @Schema(title = "FOLDER_RES_01 : 폴더 생성 응답")
    public record CreateFolderRes (
            @Schema(description = "폴더 아이디", example = "1")
            Long folderId,

            @Schema(description = "폴더 이름", example = "광고학 강의 레포트")
            String folderName,

            @Schema(description = "목표 아이디", example = "2")
            Long goalId
    ) {}

    @Schema(title = "FOLDER_RES_02 : 폴더 및 내부 할 일 조회 응답")
    public record GetFolderRes (

            @Schema(description = "목표 이름", example = "광고학 강의 레포트")
            String goalName,

            @Schema(description = "폴더 이름", example = "자료 조사")
            String folderName,

            @Schema(description = "진행 중인 할 일의 개수", example = "5")
            int progressCount,

            @Schema(description = "진행 중인 할 일 정보")
            List<TaskInfo> progressTasks,

            @Schema(description = "완료한 할 일의 개수", example = "2")
            int doneCount,

            @Schema(description = "진행 중인 할 일 정보")
            List<TaskInfo> doneTasks
    ) {}

    @Schema(title = "FOLDER_RES_02-1 : 할 일 정보")
    public record TaskInfo (
            @Schema(description = "할 일 이름", example = "경쟁사 캠페인 비교")
            String taskName,

            @Schema(description = "난이도", example = "상")
            String level,

            @Schema(description = "시간", example = "1")
            int hour,

            @Schema(description = "분", example = "10")
            int minute,

            @Schema(description = "타임 블럭에 넣은 날짜", example = "미정")
            String date
    ) {}

    @Schema(title = "FOLDER_RES_O3 : 목표 내 폴더만 조회 응답")
    public record GetFolderOnlyRes (
            @Schema(description = "목표 이름", example = "사이드 프로젝트")
            String goalName,

            @Schema(description = "폴더 이름 리스트")
            List<String> folderNames
    ) {}
}
