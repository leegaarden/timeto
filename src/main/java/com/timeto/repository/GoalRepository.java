package com.timeto.repository;

import com.timeto.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    // 사용자 ID로 목표 목록 조회
    List<Goal> findByUserId(Long userId);

    // 목표 이름으로 존재 여부 확인
    boolean existsByNameAndUserId(String name, Long userId);

    // 최신순으로 정렬된 목표 목록 조회
    List<Goal> findByUserIdOrderByCreatedAtDesc(Long userId);
}