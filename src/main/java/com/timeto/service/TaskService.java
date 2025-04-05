package com.timeto.service;

import com.timeto.config.exception.ErrorCode;
import com.timeto.config.exception.GeneralException;
import com.timeto.domain.Folder;
import com.timeto.domain.Goal;
import com.timeto.domain.Task;
import com.timeto.domain.User;
import com.timeto.domain.enums.Level;
import com.timeto.dto.task.TaskRequest;
import com.timeto.dto.task.TaskResponse;
import com.timeto.repository.FolderRepository;
import com.timeto.repository.GoalRepository;
import com.timeto.repository.TaskRepository;
import com.timeto.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final FolderRepository folderRepository;
    private final GoalRepository goalRepository;

    // 할 일 생성
    @Transactional
    public TaskResponse.CreateTaskRes createTask(TaskRequest.CreateTaskReq request, Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 폴더 조회
        Folder folder = folderRepository.findById(request.folderId())
                .orElseThrow(() -> new GeneralException(ErrorCode.FOLDER_NOT_FOUND));


        // Level 문자열을 Enum으로 변환
        Level levelEnum;
        try {
            levelEnum = Level.valueOf(request.level());
        } catch (IllegalArgumentException e) {
            throw new GeneralException(ErrorCode.INVALID_PARAMETER);
        }

        // TimeRequest에서 LocalTime으로 변환
        LocalTime taskTime = LocalTime.of(
                request.hour(),
                request.minute()
        );

        // 폴더 내 할 일 개수 조회하여 새 할 일 순서 결정
        List<Task> existingTasks = taskRepository.findByFolderId(request.folderId());
        int newOrder = existingTasks.size(); // 0부터 시작하는 인덱스라면 size가 됨

        // 할 일 생성
        Task task = Task.builder()
                .folder(folder)
                .name(request.taskName())
                .time(taskTime)
                .level(levelEnum)
                .memo(request.memo())
                .done(false)
                .displayOrder(newOrder)
                .build();

        // 저장
        Task savedTask = taskRepository.save(task);

        // 응답 생성
        return new TaskResponse.CreateTaskRes(savedTask.getId(), savedTask.getName());
    }

    // 할 일 조회
    @Transactional
    public TaskResponse.GetTaskRes getTask (Long taskId, Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 할 일 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new GeneralException(ErrorCode.TASK_NOT_FOUND));

        // 폴더 조회
        Folder folder = folderRepository.findById(task.getFolder().getId())
                .orElseThrow(() -> new GeneralException(ErrorCode.FOLDER_NOT_FOUND));

        // 목표 조회
        Goal goal = goalRepository.findById(folder.getGoal().getId())
                .orElseThrow(() -> new GeneralException(ErrorCode.GOAL_NOT_FOUND));

        return new TaskResponse.GetTaskRes(
                goal.getName(),
                folder.getName(),
                task.getName(),
                task.getTime().getHour(),
                task.getTime().getMinute(),
                task.getLevel().name(),
                task.getMemo()
        );
    }

    // 할 일 수정
    @Transactional
    public TaskResponse.EditTaskRes editTask (TaskRequest.EditTaskReq request, Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 할 일 조회
        Task task = taskRepository.findById(request.taskId())
                .orElseThrow(() -> new GeneralException(ErrorCode.TASK_NOT_FOUND));

        // 변경 사항 확인 플래그
        boolean isChanged = false;

        // 이름 변경 확인
        if (!task.getName().equals(request.taskName())) {
            task.updateName(request.taskName());
            isChanged = true;
        }

        // 시간 변경 확인
        LocalTime newTime = LocalTime.of(request.hour(), request.minute());
        if (!task.getTime().equals(newTime)) {
            task.updateTime(newTime);
            isChanged = true;
        }

        // 중요도 변경 확인
        Level newLevel;
        try {
            newLevel = Level.valueOf(request.level());
        } catch (IllegalArgumentException e) {
            throw new GeneralException(ErrorCode.INVALID_PARAMETER);
        }

        if (!task.getLevel().equals(newLevel)) {
            task.updateLevel(newLevel);
            isChanged = true;
        }

        // 메모 변경 확인 (null 고려)
        if (!Objects.equals(task.getMemo(), request.memo())) {
            task.updateMemo(request.memo());
            isChanged = true;
        }

        // 변경 사항이 없으면 예외 처리
        if (!isChanged) {
            throw new GeneralException(ErrorCode.NO_CHANGES_DETECTED);
        }

        // 변경 사항 저장
        Task updatedTask = taskRepository.save(task);

        // 응답 생성
        return new TaskResponse.EditTaskRes(
                updatedTask.getName(),
                updatedTask.getTime().getHour(),
                updatedTask.getTime().getMinute(),
                newLevel.name(),
                updatedTask.getMemo()
        );
    }
}
