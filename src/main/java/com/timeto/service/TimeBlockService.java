package com.timeto.service;

import com.timeto.config.exception.ErrorCode;
import com.timeto.config.exception.GeneralException;
import com.timeto.domain.*;
import com.timeto.domain.enums.Level;
import com.timeto.dto.timeBlock.TimeBlockRequest;
import com.timeto.dto.timeBlock.TimeBlockResponse;
import com.timeto.repository.FolderRepository;
import com.timeto.repository.TaskRepository;
import com.timeto.repository.TimeBlockRepository;
import com.timeto.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeBlockService {

    private final TimeBlockRepository timeBlockRepository;
    private final FolderRepository folderRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // 타임 블럭 및 할 일 생성
    @Transactional
    public TimeBlockResponse.CreateTimeBLockRes createTimeBlock(TimeBlockRequest.CreateTimeBlockReq request, Long userId) {

        // 사용자 조회
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

        // 폴더 조회
        Folder folder = folderRepository.findById(request.folderId())
                .orElseThrow(() -> new GeneralException(ErrorCode.FOLDER_NOT_FOUND));

        // 종료 시간이 24시를 넘어가는지 확인
        if (request.endTime().isAfter(LocalTime.of(23, 59))) {
            throw new GeneralException(ErrorCode.TIME_BLOCK_OVERLAP);
        }

        // 시간이 역전됐는지 확인
        if (request.endTime().isBefore(request.startTime())) {
            throw new GeneralException(ErrorCode.INVALID_TIME_RANGE);
        }

        // 요청 시간에 이미 타임 블럭이 존재하는지 확인
        boolean hasOverlap = timeBlockRepository.existsByDateAndTimeOverlap(
                request.date(),
                request.startTime(),
                request.endTime()
        );

        if (hasOverlap) {
            throw new GeneralException(ErrorCode.TIME_BLOCK_OVERLAP);
        }

        // Level 문자열을 Enum으로 변환
        Level levelEnum;
        try {
            levelEnum = Level.valueOf(request.level());
        } catch (IllegalArgumentException e) {
            throw new GeneralException(ErrorCode.INVALID_PARAMETER);
        }

        // 타임 블럭 생성
        TimeBlock timeBlock = TimeBlock.builder()
                .date(request.date())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .build();

        // 폴더 내 할 일 개수 조회하여 새 할 일 순서 결정
        List<Task> existingTasks = taskRepository.findByFolderIdAndDone(request.folderId(), false);
        int newOrder = existingTasks.size();

        // 할 일 생성 및 타임 블럭 연결
        Task task = Task.builder()
                .folder(folder)
                .name(request.taskName())
                .level(levelEnum)
                .time(Duration.between(request.startTime(), request.endTime()).toMinutes() > 0
                        ? LocalTime.of(
                        (int)Duration.between(request.startTime(), request.endTime()).toHours(),
                        (int)Duration.between(request.startTime(), request.endTime()).toMinutesPart())
                        : LocalTime.of(0, 0))
                .done(false)
                .displayOrder(newOrder)
                .timeBlock(timeBlock)
                .build();

        // 저장
        timeBlockRepository.save(timeBlock);
        task = taskRepository.save(task);

        // 응답 생성
        return new TimeBlockResponse.CreateTimeBLockRes(task.getId(), task.getName());
    }

    // 타임 블럭 조회
    public TimeBlockResponse.GetTimeBlockRes getTimeBlock (LocalDate date, Long userId) {

        // 사용자 조회
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

        List<Task> tasks = taskRepository.findByDateAndUserId(date, userId);
        List<TimeBlockResponse.TaskInfo> taskInfos = tasks.stream()
                .map(task -> new TimeBlockResponse.TaskInfo(
                        task.getId(),
                        task.getFolder().getGoal().getName(),
                        task.getFolder().getGoal().getColor().name(),
                        task.getName(),
                        task.getDone(),
                        task.getTimeBlock().getStartTime(),
                        task.getTimeBlock().getEndTime()
                ))
                .toList();

        return new TimeBlockResponse.GetTimeBlockRes(date, taskInfos);
    }

    // 타임 블럭 할 일 불러오기
    @Transactional
    public TimeBlockResponse.GetTaskRes loadTaskToTimeBlock(TimeBlockRequest.LoadTaskToTimeBlockReq request, Long userId) {

        // 사용자 조회
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

        // 할 일 조회
        Task task = taskRepository.findById(request.taskId())
                .orElseThrow(() -> new GeneralException(ErrorCode.TASK_NOT_FOUND));

        // 이미 타임 블럭에 연결된 할 일인지 확인
        if (task.getTimeBlock() != null) {
            throw new GeneralException(ErrorCode.TASK_ALREADY_IN_TIME_BLOCK);
        }

        // 할 일의 폴더와 목표 조회
        Folder folder = task.getFolder();
        Goal goal = folder.getGoal();

        // 요청된 날짜 사용
        LocalDate date = request.date();

        // 해당 날짜의 마지막 타임블록 조회
        LocalTime startTime;
        List<TimeBlock> existingTimeBlocks = timeBlockRepository.findByDateOrderByEndTimeDesc(date);

        if (existingTimeBlocks.isEmpty()) {
            // 해당 날짜에 타임블록이 없으면 기본 시작 시간 설정 (오전 5시)
            startTime = LocalTime.of(5, 0);
        } else {
            // 마지막 타임블록의 종료 시간을 새 타임블록의 시작 시간으로 설정
            TimeBlock lastTimeBlock = existingTimeBlocks.get(0);
            startTime = lastTimeBlock.getEndTime();
        }

        // 할 일의 소요 시간 확인 (또는 기본 시간 설정)
        LocalTime taskTime = task.getTime() != null ? task.getTime() : LocalTime.of(1, 0); // 기본 1시간

        // 종료 시간 계산
        int hours = taskTime.getHour();
        int minutes = taskTime.getMinute();

        // 시작 시간에 할 일 소요 시간을 더해 종료 시간 계산
        LocalTime endTime = startTime.plusHours(hours).plusMinutes(minutes);

        // 종료 시간이 24시를 넘어가는지 확인
        if (endTime.isAfter(LocalTime.of(23, 59))) {
            throw new GeneralException(ErrorCode.TIME_BLOCK_OVERLAP);
        }

        // 요청 시간에 이미 타임 블럭이 존재하는지 확인
        boolean hasOverlap = timeBlockRepository.existsByDateAndTimeOverlap(
                date,
                startTime,
                endTime
        );

        if (hasOverlap) {
            throw new GeneralException(ErrorCode.TIME_BLOCK_OVERLAP);
        }

        // 타임 블럭 생성
        TimeBlock timeBlock = TimeBlock.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        // 타임 블럭 저장
        timeBlockRepository.save(timeBlock);

        // 할 일에 타임 블럭 연결
        task.setTimeBlock(timeBlock);

        // 할 일 저장
        taskRepository.save(task);

        // 응답 생성
        return new TimeBlockResponse.GetTaskRes(
                task.getId(),
                goal.getName(),
                goal.getColor().name(),
                task.getName(),
                startTime,
                endTime
        );
    }

    // 타임 블럭 삭제
    @Transactional
    public Long deleteTimeBlock(Long timeBlockId, Long userId) {

        // 사용자 조회
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

        // 타임 블럭 조회
        TimeBlock timeBlock = timeBlockRepository.findById(timeBlockId)
                .orElseThrow(() -> new GeneralException(ErrorCode.TIME_BLOCK_NOT_FOUND));

        // 타임 블럭에 연결된 할 일 조회
        Task task = taskRepository.findByTimeBlockId(timeBlockId)
                .orElseThrow(() -> new GeneralException(ErrorCode.TASK_NOT_FOUND));

        // 할 일 ID
        Long taskId = task.getId();

        // 할 일 삭제 (타임 블럭도 cascade 설정에 의해 함께 삭제됨)
        taskRepository.delete(task);

        // 삭제된 할 일 ID 반환
        return taskId;
    }

    // 타임 블럭 수정
    @Transactional
    public TimeBlockResponse.EditTimeBLockRes editTimeBlock(TimeBlockRequest.EditTimeBlockReq request, Long userId) {
        // 사용자 조회
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

        // 할 일 조회
        Task task = taskRepository.findById(request.taskId())
                .orElseThrow(() -> new GeneralException(ErrorCode.TASK_NOT_FOUND));

        // 타임 블럭 조회
        TimeBlock timeBlock = task.getTimeBlock();
        if (timeBlock == null) {
            throw new GeneralException(ErrorCode.TIME_BLOCK_NOT_FOUND);
        }

        // 변경 사항 확인
        boolean hasChanges = false;

        // 할 일 이름 변경
        if (!task.getName().equals(request.taskName())) {
            task.setName(request.taskName());
            hasChanges = true;
        }

        // 시간 변경 확인
        boolean timeChanged = !timeBlock.getStartTime().equals(request.startTime()) ||
                !timeBlock.getEndTime().equals(request.endTime());

        if (timeChanged) {
            // 종료 시간이 24시를 넘어가는지 확인
            if (request.endTime().isAfter(LocalTime.of(23, 59))) {
                throw new GeneralException(ErrorCode.TIME_BLOCK_OVERLAP);
            }

            // 시간이 역전됐는지 확인
            if (request.endTime().isBefore(request.startTime())) {
                throw new GeneralException(ErrorCode.INVALID_TIME_RANGE);
            }

            // 요청 시간에 이미 타임 블럭이 존재하는지 확인 (자신의 타임 블럭은 제외)
            boolean hasOverlap = timeBlockRepository.existsByDateAndTimeOverlapExcludingId(
                    timeBlock.getDate(),
                    request.startTime(),
                    request.endTime(),
                    timeBlock.getId()
            );

            if (hasOverlap) {
                throw new GeneralException(ErrorCode.TIME_BLOCK_OVERLAP);
            }

            // 타임 블럭 시간 변경
            timeBlock.setStartTime(request.startTime());
            timeBlock.setEndTime(request.endTime());

            // 할 일의 소요 시간 업데이트
            task.setTime(LocalTime.of(
                    (int)Duration.between(request.startTime(), request.endTime()).toHours(),
                    (int)Duration.between(request.startTime(), request.endTime()).toMinutesPart()));

            hasChanges = true;
        }

        // Level 변경 확인
        Level levelEnum;
        try {
            levelEnum = Level.valueOf(request.level());
        } catch (IllegalArgumentException e) {
            throw new GeneralException(ErrorCode.INVALID_PARAMETER);
        }

        if (!task.getLevel().equals(levelEnum)) {
            task.setLevel(levelEnum);
            hasChanges = true;
        }

        // 변경 사항이 없는 경우
        if (!hasChanges) {
            throw new GeneralException(ErrorCode.NO_CHANGES_DETECTED);
        }

        // 저장
        timeBlockRepository.save(timeBlock);
        taskRepository.save(task);

        // 응답 생성
        return new TimeBlockResponse.EditTimeBLockRes(
                task.getName(),
                timeBlock.getStartTime(),
                timeBlock.getEndTime(),
                task.getLevel().name()
        );
    }
}