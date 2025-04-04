package com.timeto.service;

import com.timeto.config.exception.ErrorCode;
import com.timeto.config.exception.GeneralException;
import com.timeto.domain.*;
import com.timeto.domain.enums.Level;
import com.timeto.dto.folder.FolderRequest;
import com.timeto.dto.folder.FolderResponse;
import com.timeto.repository.FolderRepository;
import com.timeto.repository.GoalRepository;
import com.timeto.repository.TaskRepository;
import com.timeto.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final FolderRepository folderRepository;
    private final TaskRepository taskRepository;

    // 폴더 생성
    public FolderResponse.CreateFolderRes createFolder (FolderRequest.CreateFolderReq request, Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 목표 조회
        Goal goal = goalRepository.findById(request.goalId())
                .orElseThrow(() -> new GeneralException(ErrorCode.GOAL_NOT_FOUND));

        // 같은 목표 내에서 폴더 이름 중복 검사
        if (folderRepository.existsByGoalIdAndName(request.goalId(), request.folderName())) {
            throw new GeneralException(ErrorCode.DUPLICATE_FOLDER_NAME);
        }

        // 현재 목표에 속한 폴더 개수 조회하여 새 폴더 순서 결정
        List<Folder> existingFolders = folderRepository.findByGoalId(request.goalId());
        int newOrder = existingFolders.size(); // 0부터 시작하는 인덱스라면 size가 됨


        // Folder 엔티티 생성
        Folder folder = Folder.builder()
                .goal(goal)
                .name(request.folderName())
                .displayOrder(newOrder)
                .build();

        // 저장
        Folder savedFolder = folderRepository.save(folder);

        // 응답 DTO 반환
        return new FolderResponse.CreateFolderRes(
                savedFolder.getId(),
                savedFolder.getName(),
                savedFolder.getGoal().getId()
        );
    }

    // 폴더 조회
    @Transactional()
    public FolderResponse.GetFolderRes getFolder(Long folderId, Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 폴더 조회
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FOLDER_NOT_FOUND));

        Goal goal = folder.getGoal();
        if (goal == null) {throw new GeneralException(ErrorCode.GOAL_NOT_FOUND);}

        // 진행 중인 할 일 목록 조회 (정렬 없이)
        List<Task> progressTasks = taskRepository.findByFolderIdAndDone(folderId, false);

        // 완료된 할 일 목록 조회 (정렬 없이)
        List<Task> doneTasks = taskRepository.findByFolderIdAndDone(folderId, true);

        // 난이도(상-중-하) 및 생성일 기준으로 정렬
        progressTasks = sortTasksByLevelAndCreatedDate(progressTasks);
        doneTasks = sortTasksByLevelAndCreatedDate(doneTasks);

        // 할 일 정보 변환
        List<FolderResponse.TaskInfo> progressTaskInfos = progressTasks.stream()
                .map(this::createTaskInfo)
                .collect(Collectors.toList());

        List<FolderResponse.TaskInfo> doneTaskInfos = doneTasks.stream()
                .map(this::createTaskInfo)
                .collect(Collectors.toList());

        // 응답 생성
        return new FolderResponse.GetFolderRes(
                goal.getName(),
                folder.getName(),
                progressTasks.size(),
                progressTaskInfos,
                doneTasks.size(),
                doneTaskInfos
        );
    }

    // 할 일 정보 생성 메서드
    private FolderResponse.TaskInfo createTaskInfo(Task task) {
        // 날짜 정보 (타임블록 관련 로직이 없으므로 "미정"으로 설정)
        String date = "미정";

        // 타임블록 관련 로직이 구현되면 여기서 처리
        // 예: timeBlockRepository.findByTaskId(task.getId())
        //     .map(timeBlock -> timeBlock.getDate().format(DateTimeFormatter.ofPattern("MM/dd")))
        //     .orElse("미정");

        return new FolderResponse.TaskInfo(
                task.getName(),
                task.getLevel().toDisplayText(),
                task.getTime().getHour(),
                task.getTime().getMinute(),
                date
        );
    }

    // 할 일을 난이도(상-중-하) 및 생성일 기준으로 정렬하는 메서드
    private List<Task> sortTasksByLevelAndCreatedDate(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator
                        // 먼저 난이도로 정렬 (HIGH -> MIDDLE -> LOW)
                        .comparing(Task::getLevel, (l1, l2) -> {
                            if (l1 == l2) return 0;
                            if (l1 == Level.HIGH) return -1;
                            if (l1 == Level.MIDDLE && l2 == Level.LOW) return -1;
                            return 1;
                        })
                        // 같은 난이도 내에서는 생성일 기준 정렬
                        .thenComparing(BaseEntity::getCreatedAt))
                .collect(Collectors.toList());
    }
}
