package com.timeto.dto.timeBlock;

import io.swagger.v3.oas.annotations.media.Schema;

public class TimeBlockResponse {

    @Schema(title = "TIME_BLOCK_RES_01 : 타임 블럭 생성 응답")
    public record CreateTimeBLockRes (

            @Schema(description = "할 일 아이디", example = "1")
            Long taskId,

            @Schema(description = "할 일 이름", example = "경쟁사 캠페인 비교")
            String taskName
    ) {}
}
