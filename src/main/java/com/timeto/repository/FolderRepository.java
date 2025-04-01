package com.timeto.repository;

import com.timeto.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    // 목표 ID로 폴더 목록 조회
    List<Folder> findByGoalId(Long goalId);

    // 목표 ID로 폴더 목록 조회 (순서대로 정렬)
    List<Folder> findByGoalIdOrderByDisplayOrderAsc(Long goalId);

    // 목표 ID와 폴더 이름으로 존재 여부 확인
    boolean existsByGoalIdAndName(Long goalId, String name);

    // 목표 아이디로 조회
    @Query("SELECT f.id FROM Folder f WHERE f.goal.id = :goalId")
    List<Long> findIdsByGoalId(Long goalId);

    void deleteAllByIdIn(List<Long> ids);
}