package com.timeto.dto.task;

import com.timeto.config.exception.custom.annotation.ValidMemoInput;
import com.timeto.config.exception.custom.annotation.ValidTextInput;
import io.swagger.v3.oas.annotations.media.Schema;

public class TaskRequest {

    @Schema(title = "TASK_REQ_01 : 할 일 생성 요청")
    public record CreateTaskReq (
            @Schema(description = "폴더 ID", example = "1")
            Long folderId,

            @ValidTextInput
            @Schema(description = "할 일 이름", example = "경쟁사 캠페인 비교")
            String taskName,

            @Schema(description = "시간", example = "1")
            int hour,

            @Schema(description = "분", example = "30")
            int minute,

            @Schema(description = "중요도", example = "HIGH")
            String level,

            @ValidMemoInput
            @Schema(description = "메모 (선택)", example = "코카콜라 마스터피스 캠페인", nullable = true)
            String memo
    ) {}

    @Schema(title = "TASK_REQ_02 : 할 일 수정 요청")
    public record EditTaskReq (

            @Schema(description = "수정할 할 일 아이디", example = "1")
            Long taskId,

            @ValidTextInput
            @Schema(description = "할 일 이름", example = "경쟁사 캠페인 비교")
            String taskName,

            @Schema(description = "시간", example = "1")
            int hour,

            @Schema(description = "분", example = "30")
            int minute,

            @Schema(description = "중요도", example = "HIGH")
            String level,

            @ValidMemoInput
            @Schema(description = "메모 (선택)", example = "코카콜라 마스터피스 캠페인", nullable = true)
            String memo
    ) {}
}
