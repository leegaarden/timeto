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
}
