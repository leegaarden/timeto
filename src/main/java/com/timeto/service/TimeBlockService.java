package com.timeto.service;

import com.timeto.config.exception.ErrorCode;
import com.timeto.config.exception.GeneralException;
import com.timeto.domain.Folder;
import com.timeto.domain.Task;
import com.timeto.domain.TimeBlock;
import com.timeto.domain.User;
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

    @Transactional
    public TimeBlockResponse.CreateTimeBLockRes createTimeBlock(TimeBlockRequest.CreateTimeBlockReq request, Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 폴더 조회
        Folder folder = folderRepository.findById(request.folderId())
                .orElseThrow(() -> new GeneralException(ErrorCode.FOLDER_NOT_FOUND));

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

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
}