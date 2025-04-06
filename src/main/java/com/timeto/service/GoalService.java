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

import java.util.ArrayList;
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
    public GoalResponse.GetUserGoalRes getUserGoals(Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 사용자의 모든 목표 조회
        List<Goal> goals = goalRepository.findByUserId(userId);

        // 각 목표에 대한 폴더 및 할 일 정보를 포함한 응답 생성
        List<GoalResponse.GoalFolder> goalsFoldersList = goals.stream()
                .map(goal -> {
                    // 목표에 속한 폴더 목록을 displayOrder 순서대로 조회
                    List<Folder> folders = folderRepository.findByGoalIdOrderByDisplayOrderAsc(goal.getId());

                    // 각 폴더에 대한 할 일 개수 계산 및 응답 DTO 생성
                    List<GoalResponse.FolderInfo> folderInfoList = folders.stream()
                            .map(folder -> {
                                // 폴더에 속한 할 일 개수 조회
                                int taskCount = taskRepository.countByFolderId(folder.getId());

                                return new GoalResponse.FolderInfo(
                                        folder.getId(),
                                        folder.getName(),
                                        taskCount
                                );
                            })
                            .collect(Collectors.toList());

                    // 각 목표별 응답 생성
                    return new GoalResponse.GoalFolder(
                            goal.getId(),
                            goal.getName(),
                            goal.getColor().name(),
                            folderInfoList
                    );
                })
                .collect(Collectors.toList());

        // 최종 응답 생성
        return new GoalResponse.GetUserGoalRes(goalsFoldersList);
    }

    // 목표만 조회
    @Transactional(readOnly = true)
    public GoalResponse.GetGoalOnlyRes getUserGoalsOnly(Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 사용자의 모든 목표 조회
        List<Goal> goals = goalRepository.findByUserId(userId);

        // 목표 정보만 추출하여 DTO 변환
        List<GoalResponse.GoalInfo> goalInfos = goals.stream()
                .map(goal -> new GoalResponse.GoalInfo(
                        goal.getId(),
                        goal.getName(),
                        goal.getColor().name()
                ))
                .collect(Collectors.toList());

        // 목표 목록을 포함한 응답 객체 생성
        return new GoalResponse.GetGoalOnlyRes(goalInfos);
    }

    // 목표 이름 변경
    @Transactional
    public GoalResponse.EditGoalNameRes editGoalName(GoalRequest.EditGoalNameReq request, Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 목표 조회
        Goal goal = goalRepository.findById(request.goalId())
                .orElseThrow(() -> new GeneralException(ErrorCode.GOAL_NOT_FOUND));

        // 현재 이름과 변경하려는 이름이 같은지 확인
        if (goal.getName().equals(request.goalName())) {
            throw new GeneralException(ErrorCode.SAME_GOAL_NAME);
        }

        // 이름 중복 검사
        if (goalRepository.existsByNameAndUserId(request.goalName(), userId)) {
            throw new GoalException(ErrorCode.DUPLICATE_GOAL_NAME);
        }

        // 목표 이름 변경
        goal.updateName(request.goalName());
        Goal updatedGoal = goalRepository.save(goal);

        // 응답 생성
        return new GoalResponse.EditGoalNameRes(updatedGoal.getId(), updatedGoal.getName());
    }

    // 목표 색상 변경
    @Transactional
    public GoalResponse.EditGoalColorRes editGoalColor(GoalRequest.EditGoalColorReq request, Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 목표 조회
        Goal goal = goalRepository.findById(request.goalId())
                .orElseThrow(() -> new GeneralException(ErrorCode.GOAL_NOT_FOUND));

        // 현재 색상과 변경하려는 색상이 같은지 확인
        if (goal.getColor().name().equals(request.color())) {
            throw new GeneralException(ErrorCode.SAME_GOAL_COLOR);
        }

        // 목표 색상 변경
        goal.updateColor(request.color());
        Goal updatedGoal = goalRepository.save(goal);

        // 응답 생성
        return new GoalResponse.EditGoalColorRes(updatedGoal.getId(), updatedGoal.getColor().name());
    }

    // 목표 삭제
    @Transactional
    public GoalResponse.DeleteGoalRes deleteGoal(Long goalId, Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 목표 조회
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GeneralException(ErrorCode.GOAL_NOT_FOUND));

        // 목표에 속한 폴더 ID 목록 조회
        List<Long> folderIds = folderRepository.findIdsByGoalId(goalId);

        // 각 폴더에 속한 할 일 ID 목록 조회
        List<Long> taskIds = new ArrayList<>();
        for (Long folderId : folderIds) {
            List<Long> folderTaskIds = taskRepository.findIdsByFolderId(folderId);
            taskIds.addAll(folderTaskIds);
        }

        // 타임 블럭, 할 일, 폴더, 목표 순서로 삭제 (외래 키 제약조건 때문)
        if (!taskIds.isEmpty()) {
            taskRepository.deleteAllByIdIn(taskIds);
        }

        if (!folderIds.isEmpty()) {
            folderRepository.deleteAllByIdIn(folderIds);
        }

        goalRepository.delete(goal);

        // 응답 생성
        return new GoalResponse.DeleteGoalRes(goalId, folderIds, taskIds);
    }
}