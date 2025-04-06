package com.timeto.repository;

import com.timeto.domain.Task;
import com.timeto.domain.mapping.TaskTimeBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskTimeBlockRepository extends JpaRepository<TaskTimeBlock, Long> {

    // TaskTimeBlockRepository 인터페이스에 추가
    @Query("SELECT ttb.timeBlock.id FROM TaskTimeBlock ttb WHERE ttb.task.id = :taskId")
    List<Long> findTimeBlockIdsByTaskId(Long taskId);

    void deleteByTaskId(Long taskId);
}
