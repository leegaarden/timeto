package com.timeto.dto.goal;

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
    public record GetUserGoalRes(
            List<GoalsFolders> goalsFoldersList
    ) {}

    @Schema(title = "GOAL_RES_02-1 : 목표와 폴더 조회 응답")
    public record GoalsFolders (

            @Schema(description = "목표 아이디", example = "1")
            Long goalId,

            @Schema(description = "목표 이름", example = "광고학 강의 레포트")
            String goalName,

            @Schema(description = "목표 색상", example = "RED01")
            String color,

            @Schema(description = "폴더 이름 및 할 일 개수")
            List<FolderInfo> folderInfoList

    ) {}

    @Schema(title = "GOAL_RES_02-2 : 목표 내 폴더 이름 및 할 일 개수")
    public record FolderInfo(

            @Schema(description = "폴더 이름", example = "자료 구조")
            String folderName,

            @Schema(description = "할 일 개수", example = "4")
            int taskCount
    ) {}

    @Schema(title = "GOAL_RES_03 : 목표 목록 조회 응답")
    public record GetGoalsOnlyRes (
            @Schema(description = "목표 목록")
            List<GoalInfo> goals
    ) {}

    @Schema(title = "GOAL_RES_03-1 : 목표 정보")
    public record GoalInfo (
            @Schema(description = "목표 아이디", example = "1")
            Long goalId,

            @Schema(description = "목표 이름", example = "광고학 강의 레포트")
            String goalName,

            @Schema(description = "목표 색상", example = "RED01")
            String color
    ) {}

    @Schema(title = "GOAL_RES_04 : 목표 이름 변경 응답")
    public record EditGoalNameRes(
            @Schema(description = "목표 아이디", example = "1")
            Long goalId,

            @Schema(description = "변경된 이름", example = "경영학 강의 레포트")
            String goalName
    ) {}

    @Schema(title = "GOAL_RES_05 : 목표 색상 변경 응답")
    public record EditGoalColorRes (
            @Schema(description = "목표 아이디", example = "1")
            Long goalId,

            @Schema(description = "변경된 색상", example = "GREEN01")
            String color
    ) {}

    @Schema(title = "GOAL_RES_06 : 목표 삭제 응답")
    public record DeleteGoalRes (
            @Schema(description = "삭제된 목표 ID", example = "1")
            Long goalId,

            @Schema(description = "삭제된 폴더 ID 목록", example = "[1, 2, 3]")
            List<Long> deletedFolderIds,

            @Schema(description = "삭제된 할 일 ID 목록", example = "[10, 11, 12, 13]")
            List<Long> deletedTaskIds
    ) {}

}
