package com.timeto.dto.timeBlock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.timeto.config.exception.custom.annotation.ValidTextInput;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeBlockRequest {

    @Schema(title = "TIME_BLOCK_REQ_01 : 타임 블럭 생성 요청")
    public record CreateTimeBlockReq (

            @Schema(description = "날짜")
            LocalDate date,

            @Schema(description = "폴더 ID", example = "1")
            Long folderId,

            @ValidTextInput
            @Schema(description = "할 일 이름", example = "경쟁사 캠페인 비교")
            String taskName,

            @JsonFormat(pattern = "H:mm", shape = JsonFormat.Shape.STRING)
            @Schema(description = "시작 시간", example = "6:00")
            LocalTime startTime,

            @JsonFormat(pattern = "H:mm", shape = JsonFormat.Shape.STRING)
            @Schema(description = "종료 시간", example = "8:00")
            LocalTime endTime,

            @Schema(description = "중요도", example = "HIGH")
            String level
    ) {}
}
