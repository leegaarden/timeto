package com.timeto.service;

import com.timeto.config.exception.ErrorCode;
import com.timeto.config.exception.GeneralException;
import com.timeto.config.exception.custom.GoalException;
import com.timeto.domain.Folder;
import com.timeto.domain.Goal;
import com.timeto.domain.User;
import com.timeto.domain.enums.Color;
import com.timeto.dto.goal.GoalRequest;
import com.timeto.dto.goal.GoalResponse;
import com.timeto.repository.FolderRepository;
import com.timeto.repository.GoalRepository;
import com.timeto.repository.TaskRepository;
import com.timeto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final TaskRepository taskRepository;

    // 목표 생성
    @Transactional
    public GoalResponse.CreateGoalRes createGoal(GoalRequest.CreateGoalReq request, Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

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
                savedGoal.getId(),
                savedGoal.getName(),
                savedGoal.getColor().name()
        );
    }

    // 사용자 목표 조회
    @Transactional(readOnly = true)
    public GoalResponse.GetUserGoalsRes getUserGoals(Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 사용자의 모든 목표 조회
        List<Goal> goals = goalRepository.findByUserId(userId);

        // 각 목표에 대한 폴더 및 할 일 정보를 포함한 응답 생성
        List<GoalResponse.GoalsFolders> goalsFoldersList = goals.stream()
                .map(goal -> {
                    // 목표에 속한 폴더 목록을 displayOrder 순서대로 조회
                    List<Folder> folders = folderRepository.findByGoalIdOrderByDisplayOrderAsc(goal.getId());

                    // 각 폴더에 대한 할 일 개수 계산 및 응답 DTO 생성
                    List<GoalResponse.Folders> foldersList = folders.stream()
                            .map(folder -> {
                                // 폴더에 속한 할 일 개수 조회
                                int taskCount = taskRepository.countByFolderId(folder.getId());

                                return new GoalResponse.Folders(
                                        folder.getName(),
                                        taskCount
                                );
                            })
                            .collect(Collectors.toList());

                    // 각 목표별 응답 생성
                    return new GoalResponse.GoalsFolders(
                            goal.getName(),
                            goal.getColor().name(),
                            foldersList
                    );
                })
                .collect(Collectors.toList());

        // 최종 응답 생성
        return new GoalResponse.GetUserGoalsRes(goalsFoldersList);
    }
}