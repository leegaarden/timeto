package com.timeto.dto.timeBlock;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TimeBlockResponse {

    @Schema(title = "TIME_BLOCK_RES_01 : 타임 블럭 생성 응답")
    public record CreateTimeBLockRes (

            @Schema(description = "할 일 아이디", example = "1")
            Long taskId,

            @Schema(description = "할 일 이름", example = "경쟁사 캠페인 비교")
            String taskName
    ) {}

    @Schema(title = "TIME_BLOCK_RES_02 : 타임 블럭 조회 응답")
    public record GetTimeBlockRes (
            @Schema(description = "오늘 날짜")
            LocalDate now,

            @Schema(description = "할 일 정보들")
            List<TaskInfo> taskInfos

    ) {}

    @Schema(title = "TIME_BLOCK_RES_02-1 : 할 일 정보")
    public record TaskInfo (

            @Schema(description = "할 일 아이디", example = "1")
            Long taskId,

            @Schema(description = "목표 이름", example = "사이드 프로젝트")
            String goalName,

            @Schema(description = "목표 색상", example = "RED01")
            String color,

            @Schema(description = "할 일 이름", example = "경쟁사 캠페인 비교")
            String taskName,

            @Schema(description = "완료 여부", example = "false")
            boolean done,

            @JsonFormat(pattern = "H:mm", shape = JsonFormat.Shape.STRING)
            @Schema(description = "시작 시간", example = "6:00")
            LocalTime startTime,

            @JsonFormat(pattern = "H:mm", shape = JsonFormat.Shape.STRING)
            @Schema(description = "종료 시간", example = "8:00")
            LocalTime endTime
    ) {}

    @Schema(title = "TIME_BLOCK_RES_03 : 타임 블럭 할 일 불러오기 응답")
    public record GetTaskRes (
            @Schema(description = "할 일 아이디", example = "1")
            Long taskId,

            @Schema(description = "목표 이름", example = "사이드 프로젝트")
            String goalName,

            @Schema(description = "목표 색상", example = "RED01")
            String color,

            @Schema(description = "할 일 이름", example = "경쟁사 캠페인 비교")
            String taskName,

            @JsonFormat(pattern = "H:mm", shape = JsonFormat.Shape.STRING)
            @Schema(description = "시작 시간", example = "6:00")
            LocalTime startTime,

            @JsonFormat(pattern = "H:mm", shape = JsonFormat.Shape.STRING)
            @Schema(description = "종료 시간", example = "8:00")
            LocalTime endTime
    ) {}

    @Schema(title = "TIME_BLOCK_RES_04 : 타임 블럭 수정 응답")
    public record EditTimeBLockRes (
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

    @Schema(title = "TIME_BLOCK_RES_05 : 타임 블럭 순서 이동 응답")
    public record EditTimeBLockOrderRes (

            @JsonFormat(pattern = "H:mm", shape = JsonFormat.Shape.STRING)
            @Schema(description = "시작 시간", example = "6:00")
            LocalTime startTime,

            @JsonFormat(pattern = "H:mm", shape = JsonFormat.Shape.STRING)
            @Schema(description = "종료 시간", example = "8:00")
            LocalTime endTime
    ) {}
}
