package com.timeto.repository;

import com.timeto.domain.TimeBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TimeBlockRepository extends JpaRepository<TimeBlock, Long> {

    @Query("SELECT COUNT(tb) > 0 FROM TimeBlock tb WHERE tb.date = :date AND NOT (" +
            ":startTime >= tb.endTime OR :endTime <= tb.startTime)")
    boolean existsByDateAndTimeOverlap(LocalDate date, LocalTime startTime, LocalTime endTime);

    TimeBlock findByTaskId(Long taskId);

    // 특정 날짜의 타임블록을 종료 시간 기준 내림차순으로 조회
    List<TimeBlock> findByDateOrderByEndTimeDesc(LocalDate date);

    // 특정 날짜에 시간이 겹치는 타임블록이 있는지 확인 (특정 ID 제외)
    @Query("SELECT COUNT(t) > 0 FROM TimeBlock t WHERE t.date = :date " +
            "AND t.id != :excludeId " +
            "AND ((t.startTime <= :endTime AND t.endTime >= :startTime))")
    boolean existsByDateAndTimeOverlapExcludingId(
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            Long excludeId
    );
}
