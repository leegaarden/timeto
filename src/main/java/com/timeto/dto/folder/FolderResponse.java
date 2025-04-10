package com.timeto.dto.folder;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.swing.plaf.PanelUI;
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

            @Schema(description = "목표 색상", example = "RED01")
            String color,

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

            @Schema(description = "완료한 할 일 정보")
            List<TaskInfo> doneTasks
    ) {}

    @Schema(title = "FOLDER_RES_02-1 : 할 일 정보")
    public record TaskInfo (

            @Schema(description = "할 일 아이디", example = "1")
            Long taskId,

            @Schema(description = "할 일 이름", example = "경쟁사 캠페인 비교")
            String taskName,

            @Schema(description = "중요도", example = "HIGH")
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

            @Schema(description = "목표 색상", example = "RED01")
            String color,

            @Schema(description = "목표 이름", example = "사이드 프로젝트")
            String goalName,

            @Schema(description = "폴더 리스트")
            List<FolderInfo> folders
    ) {}

    @Schema(title = "FOLDER_RES_03-1 : 폴더 정보")
    public record FolderInfo (
            @Schema(description = "폴더 아이디", example = "1")
            Long folderId,

            @Schema(description = "폴더 이름", example = "기획 및 설계")
            String folderName
    ) {}

    @Schema(title = "FOLDER_RES_04 : 폴더 이름 변경 응답")
    public record EditFolderNameRes(
            @Schema(description = "폴더 아이디", example = "1")
            Long folderId,

            @Schema(description = "변경된 이름", example = "브랜드별 소셜 마케팅 전략 조사")
            String folderName
    ) {}

    @Schema(title = "FOLDER_RES_05 : 폴더 삭제 응답")
    public record DeleteFolderRes (
            @Schema(description = "삭제된 폴더 아이디", example = "1")
            Long folderId,

            @Schema(description = "삭제된 할 일 아이디 목록", example = "[10, 11, 12, 13]")
            List<Long> deletedTaskIds,

            @Schema(description = "삭제된 타임 블럭 아이디 목록", example = "[20, 21, 22, 23]")
            List<Long> deletedTimeBlockIds
    ) {}

    @Schema(title = "FOLDER_RES_06 : 폴더 순서 변경 응답")
    public record EditFolderOrderRes (

            @Schema(description = "변경된 순서의 폴더 아이디", example = "[14, 11, 12, 13]")
            List<Long> folderIds
    ) {}
}

