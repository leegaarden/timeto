package com.timeto.repository;

import com.timeto.domain.TimeBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeBlockRepository extends JpaRepository<TimeBlock, Long> {
}
