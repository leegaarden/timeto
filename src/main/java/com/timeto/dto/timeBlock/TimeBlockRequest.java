package com.timeto.dto.timeBlock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.timeto.config.exception.custom.annotation.ValidMemoInput;
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

    @Schema(title = "TIME_BLOCK_REQ_02 : 타임 블럭 할 일 불러오기 요청")
    public record LoadTaskToTimeBlockReq (
            @Schema(description = "날짜")
            LocalDate date,

            @Schema(description = "할 일 ID", example = "1")
            Long taskId
    ) {}

    @Schema(title = "TIME_BLOCK_REQ_03 : 타임 블럭 할 일 수정 요청")
    public record EditTimeBlockReq (

            @Schema(description = "수정할 할 일 아이디", example = "1")
            Long taskId,

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

    @Schema(title = "TIME_BLOCK_REQ_04 : 타임 블럭 순서 이동 요청")
    public record EditTImeBlockOrderReq (

            @Schema(description = "수정할 할 일 아이디", example = "1")
            Long taskId,

            @JsonFormat(pattern = "H:mm", shape = JsonFormat.Shape.STRING)
            @Schema(description = "이동하려는 블럭의 시작 시간", example = "6:00")
            LocalTime startTime
    ) {}

}
