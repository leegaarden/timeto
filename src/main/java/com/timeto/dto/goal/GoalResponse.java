package com.timeto.dto.goal;

import com.timeto.config.exception.custom.annotation.ValidTextInput;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class GoalResponse {

    @Schema(title = "GOAL_RES_01 : 목표 생성 응답")
    public record CreateGoalRes (

            @Schema(description = "목표 아이디", example = "1")
            Long goalId,

            @Schema(description = "목표 이름", example = "광고학 강의 레포트")
            String goalName,

            @Schema(description = "목표 색상", example = "RED01")
            String color
    ) {}

    @Schema(title = "GOAL_RES_02 : 사용자의 목표 조회 응답")
    public record GetUserGoalsRes (
            List<GoalsFolders> goalsFoldersList
    ) {}

    @Schema(title = "GOAL_RES_02-1 : 목표와 폴더 조회 응답")
    public record GoalsFolders (

            @Schema(description = "목표 이름", example = "광고학 강의 레포트")
            String gaolName,

            @Schema(description = "목표 색상", example = "RED01")
            String color,

            @Schema(description = "폴더 이름 및 할 일 개수")
            List<Folders> foldersList

    ) {}

    @Schema(title = "GOAL_RES_02-2 : 목표 내 폴더 이름 및 할 일 개수")
    public record Folders (

            @Schema(description = "폴더 이름", example = "자료 구조")
            String folderName,

            @Schema(description = "할 일 개수", example = "4")
            int taskCount
    ) {}

}
