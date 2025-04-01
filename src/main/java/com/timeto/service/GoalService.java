package com.timeto.service;

import com.timeto.config.exception.ErrorCode;
import com.timeto.config.exception.custom.GoalException;
import com.timeto.domain.Goal;
import com.timeto.domain.User;
import com.timeto.domain.enums.Color;
import com.timeto.dto.goal.GoalRequest;
import com.timeto.dto.goal.GoalResponse;
import com.timeto.repository.GoalRepository;
import com.timeto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Transactional
    public GoalResponse.CreateGoalRes createGoal(GoalRequest.CreateGoalReq request, Long userId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

        // 이름 중복 검사
        if (goalRepository.existsByNameAndUserId(request.goalName(), userId)) {
            throw new GoalException(ErrorCode.DUPLICATE_GOAL_NAME);
        }

        // String을 Color Enum으로 변환
        Color colorEnum = Color.valueOf(request.color());

        // Goal 엔티티 생성
        Goal goal = Goal.builder()
                .name(request.goalName())
                .color(colorEnum)
                .user(user)
                .build();

        // 저장
        Goal savedGoal = goalRepository.save(goal);

        // 응답 DTO 반환
        return new GoalResponse.CreateGoalRes(
                savedGoal.getName(),
                savedGoal.getColor().name()
        );
    }
}