package com.timeto.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

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
}
