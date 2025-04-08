package com.timeto.repository;

import com.timeto.domain.TimeBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;

public interface TimeBlockRepository extends JpaRepository<TimeBlock, Long> {

    @Query("SELECT COUNT(tb) > 0 FROM TimeBlock tb WHERE tb.date = :date AND NOT (" +
            ":startTime >= tb.endTime OR :endTime <= tb.startTime)")
    boolean existsByDateAndTimeOverlap(LocalDate date, LocalTime startTime, LocalTime endTime);

    TimeBlock findByTaskId(Long taskId);
}
