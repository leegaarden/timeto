package com.timeto.dto.task;

import com.timeto.dto.folder.FolderResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class TaskResponse {

    @Schema(title = "TASK_RES_01 : 할 일 생성 응답")
    public record CreateTaskRes (

            @Schema(description = "할 일 아이디", example = "1")
            Long taskId,

            @Schema(description = "할 일 이름", example = "경쟁사 캠페인 비교")
            String taskName
    ) {}

    @Schema(title = "TASK_RES_02 : 할 일 조회 응답")
    public record GetTaskRes (

            @Schema(description = "목표 색상", example = "RED01")
            String color,

            @Schema(description = "목표 이름", example = "광고학 강의 레포트")
            String goalName,

            @Schema(description = "폴더 이름", example = "자료 조사")
            String folderName,

            @Schema(description = "할 일 이름", example = "경쟁사 캠페인 비교")
            String taskName,

            @Schema(description = "시간", example = "1")
            int hour,

            @Schema(description = "분", example = "30")
            int minute,

            @Schema(description = "중요도", example = "HIGH")
            String level,

            @Schema(description = "메모", example = "다현아 화이팅")
            String memo
    ) {}

    @Schema(title = "TASK_RES_03 : 할 일 수정 응답")
    public record EditTaskRes (

            @Schema(description = "할 일 이름", example = "경쟁사 캠페인 비교")
            String taskName,

            @Schema(description = "시간", example = "1")
            int hour,

            @Schema(description = "분", example = "30")
            int minute,

            @Schema(description = "중요도", example = "HIGH")
            String level,

            @Schema(description = "메모", example = "다현아 화이팅")
            String memo
    ) {}

    @Schema(title = "TASK_RES_04 : 할 일 삭제 응답")
    public record DeleteTaskRes (
            @Schema(description = "할 일 아이디", example = "1")
            Long taskId,

            @Schema(description = "타임 블럭 아이디(없을 경우 -1 반환)", example = "1")
            Long timeBlockId
    ) {}

    @Schema(title = "TASK_RES_05 : 할 일 순서 변경 응답")
    public record EditTaskOrderRes (

            @Schema(description = "변경된 순서의 할 일 아이디", example = "[14, 11, 12, 13]")
            List<Long> taskIds
    ) {}

    @Schema(title = "TASK_RES_06 : 진행 중인 할 일만 조회 응답")
    public record GetOnlyProgressTaskRes (

            @Schema(description = "목표 색상", example = "RED01")
            String color,

            @Schema(description = "폴더 이름", example = "자료 조사")
            String folderName,

            @Schema(description = "진행 중인 할 일의 개수", example = "5")
            int progressCount,

            @Schema(description = "진행 중인 할 일 정보")
            List<TaskInfo> progressTasks
    ) {}

    @Schema(title = "TASK_RES_06-1 : 할 일 정보")
    public record TaskInfo (
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
}
