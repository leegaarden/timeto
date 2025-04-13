package com.timeto.service;

import com.timeto.config.exception.ErrorCode;
import com.timeto.config.exception.GeneralException;
import com.timeto.domain.*;
import com.timeto.domain.enums.Level;
import com.timeto.dto.folder.FolderResponse;
import com.timeto.dto.task.TaskRequest;
import com.timeto.dto.task.TaskResponse;
import com.timeto.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final FolderRepository folderRepository;
    private final GoalRepository goalRepository;
    private final FolderService folderService;

    // 할 일 생성
    @Transactional
    public TaskResponse.CreateTaskRes createTask(TaskRequest.CreateTaskReq request, Long userId) {

        // 사용자 조회
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

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
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

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
                goal.getColor().name(),
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
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

        // 할 일 조회
        Task task = taskRepository.findById(request.taskId())
                .orElseThrow(() -> new GeneralException(ErrorCode.TASK_NOT_FOUND));

        // 완료된 할 일인지 학인
        if(task.getDone()) {throw new GeneralException(ErrorCode.INVALID_DONE_CHANGES);}

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

    // 할 일 삭제
    public TaskResponse.DeleteTaskRes deleteTask (Long taskId, Long userId) {

        // 사용자 조회
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

        // 할 일 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new GeneralException(ErrorCode.TASK_NOT_FOUND));

        // 타임 블럭 조회
        TimeBlock timeBlock = task.getTimeBlock();
        Long timeBlockId;
        timeBlockId = (timeBlock == null) ? (long) -1 : timeBlock.getId();

        taskRepository.delete(task);

        return new TaskResponse.DeleteTaskRes(task.getFolder().getId(), taskId, timeBlockId);
    }

    // 할 일 완료
    public Long doneTask (Long taskId, Long userId) {

        // 사용자 조회
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

        // 할 일 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new GeneralException(ErrorCode.TASK_NOT_FOUND));

        if(task.getDone()) {throw new GeneralException(ErrorCode.INVALID_DONE);}
        task.updateDone();
        taskRepository.save(task);

        return taskId;
    }

    // 할 일 순서 변경
    public TaskResponse.EditTaskOrderRes editTaskOrder (TaskRequest.EditTaskOrderReq request, Long userId) {

        // 사용자 조회
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

        // 순서 변경할 할 일 조회
        Task targetTask = taskRepository.findById(request.taskId())
                .orElseThrow(() -> new GeneralException(ErrorCode.TASK_NOT_FOUND));

        // 할 일의 폴더
        Folder folder = targetTask.getFolder();

        // 완료된 할 일이면 순서 변경 불가
        if (targetTask.getDone()) {
            throw new GeneralException(ErrorCode.INVALID_DONE_CHANGES);
        }
        // 현재 할 일의 순서
        Integer currentOrder = targetTask.getDisplayOrder();

        // 변경하려는 순서가 현재 순서와 같으면 변경 불필요
        if (currentOrder != null && currentOrder == request.changeOrder()) {
            List<Long> currentOrderIds = taskRepository.findByFolderIdAndDoneOrderByDisplayOrderAsc(folder.getId(), false)
                    .stream()
                    .map(Task::getId)
                    .collect(Collectors.toList());
            return new TaskResponse.EditTaskOrderRes(currentOrderIds);
        }

        // 같은 폴더에 속한 진행 중인 할 일만 조회 (순서대로)
        List<Task> tasks = taskRepository.findByFolderIdAndDoneOrderByDisplayOrderAsc(folder.getId(), false);

        // 폴더에 진행 중인 할 일이 없는 경우
        if (tasks.isEmpty()) {
            throw new GeneralException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 변경하려는 위치가 범위를 벗어나는 경우
        if (request.changeOrder() < 0 || request.changeOrder() >= tasks.size()) {
            throw new GeneralException(ErrorCode.INVALID_PARAMETER);
        }

        // 할 일 순서 변경 로직
        // 1. 현재 순서에서 할 일 제거
        tasks.remove(targetTask);

        // 2. 새 위치에 할 일 삽입
        tasks.add(request.changeOrder(), targetTask);

        // 3. 모든 할 일의 순서 업데이트
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            task.updateOrder(i);
            taskRepository.save(task);
        }

        // 4. 변경된 순서의 할 일 ID 목록 생성
        List<Long> updatedTaskIds = tasks.stream()
                .map(Task::getId)
                .collect(Collectors.toList());

        return new TaskResponse.EditTaskOrderRes(updatedTaskIds);
    }

    // 진행 중인 할 일만 조회
    @Transactional()
    public TaskResponse.GetOnlyProgressTaskRes getOnlyProgressTask(Long folderId, Long userId) {

        // 사용자 조회
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

        // 폴더 조회
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FOLDER_NOT_FOUND));

        Goal goal = folder.getGoal();
        if (goal == null) {throw new GeneralException(ErrorCode.GOAL_NOT_FOUND);}

        // 진행 중인 할 일 목록 조회 (정렬 없이)
        List<Task> progressTasks = taskRepository.findByFolderIdAndDone(folderId, false);

        // 난이도(상-중-하) 및 생성일 기준으로 정렬
        progressTasks = folderService.sortTasksByLevelAndCreatedDate(progressTasks);
        // 할 일 정보 변환
        List<TaskResponse.TaskInfo> progressTaskInfos = progressTasks.stream()
                .map(this::createTaskInfo)
                .collect(Collectors.toList());

        // 응답 생성
        return new TaskResponse.GetOnlyProgressTaskRes(
                goal.getColor().name(),
                folder.getName(),
                progressTasks.size(),
                progressTaskInfos
        );
    }
    // 할 일 정보 생성
    public TaskResponse.TaskInfo createTaskInfo(Task task) {

        String date = "미정";

        // 타임블록이 있는 경우, 날짜 정보 포맷팅
        TimeBlock timeBlock = task.getTimeBlock();
        if (timeBlock != null) {
            // 날짜를 확인
            LocalDate blockDate = timeBlock.getDate();
            if (blockDate != null) {
                LocalDate today = LocalDate.now();
                LocalDate tomorrow = today.plusDays(1);

                if (blockDate.isEqual(today)) {
                    date = "오늘";
                } else if (blockDate.isEqual(tomorrow)) {
                    date = "내일";
                } else {
                    // 그 외의 날짜는 "M/d(E)" 형식으로 포맷팅 (예: 3/29(토))
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d(E)", Locale.KOREAN);
                    date = blockDate.format(formatter);
                }
            }
        }

        return new TaskResponse.TaskInfo(
                task.getName(),
                task.getLevel().name(),
                task.getTime().getHour(),
                task.getTime().getMinute(),
                date
        );
    }}
