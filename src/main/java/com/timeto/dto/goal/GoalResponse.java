package com.timeto.dto.goal;

import com.timeto.config.exception.custom.annotation.ValidTextInput;
import io.swagger.v3.oas.annotations.media.Schema;

public class GoalResponse {

    @Schema(title = "GOAL_RES_01 : 목표 생성 응답")
    public record CreateGoalRes (

            @Schema(description = "목표 이름", example = "광고학 강의 레포트")
            String goalName,

            @Schema(description = "목표 색상", example = "RED01")
            String color
    ) {}

}
