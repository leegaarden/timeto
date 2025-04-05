package com.timeto.dto.goal;

import com.timeto.config.exception.custom.annotation.ValidTextInput;
import io.swagger.v3.oas.annotations.media.Schema;

public class GoalRequest {

    @Schema(title = "GOAL_REQ_01 : 목표 생성 요청")
    public record CreateGoalReq (

            @ValidTextInput
            @Schema(description = "목표 이름", example = "광고학 강의 레포트")
            String goalName,

            @Schema(description = "목표 색상", example = "RED01")
            String color
    ) {}

    @Schema(title = "GOAL_REQ_02 : 목표 이름 변경 요청")
    public record EditGoalNameReq (
            @Schema(description = "목표 아이디", example = "1")
            Long goalId,

            @Schema(description = "변경할 이름", example = "경영학 강의 레포트")
            String goalName

    ) {}

    @Schema(title = "GOAL_REQ_03 : 목표 이름 변경 요청")
    public record EditGoalColorReq (
            @Schema(description = "목표 아이디", example = "1")
            Long goalId,

            @Schema(description = "변경할 색상", example = "GREEN01")
            String color

    ) {}

}
