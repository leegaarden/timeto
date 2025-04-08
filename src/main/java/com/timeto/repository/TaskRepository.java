package com.timeto.repository;

import com.timeto.domain.Folder;
import com.timeto.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // 폴더 ID로 할 일 목록 조회
    List<Task> findByFolderId(Long folderId);

    // 폴더 ID로 할 일 개수 조회
    int countByFolderId(Long folderId);

    // 폴더 ID와 완료 여부로 할 일 목록 조회
    List<Task> findByFolderIdAndDone(Long folderId, Boolean done);

    // 폴더 ID와 완료 여부로 할 일 개수 조회
    int countByFolderIdAndDone(Long folderId, Boolean done);

    // displayOrder 기준으로 정렬된 할 일 목록 조회
    List<Task> findByFolderIdOrderByDisplayOrderAsc(Long folderId);

    // 특정 사용자의 모든 할 일 조회 (목표와 폴더를 조인하여)
    List<Task> findByFolder_Goal_UserId(Long userId);

    // 폴더 아이디로 조회
    @Query("SELECT t.id FROM Task t WHERE t.folder.id = :folderId")
    List<Long> findIdsByFolderId(Long folderId);

    void deleteAllByIdIn(List<Long> ids);

    // TaskRepository 인터페이스에 추가
    List<Task> findByFolderIdAndDoneOrderByDisplayOrderAsc(Long folderId, Boolean done);

    @Query("SELECT t.timeBlock.id FROM Task t WHERE t.id IN :taskIds AND t.timeBlock IS NOT NULL")
    List<Long> findTimeBlockIdsByTaskIds(List<Long> taskIds);

    // 특정 사용자의 날짜별 할 일 조회
    @Query("SELECT t FROM Task t JOIN t.timeBlock tb JOIN t.folder f WHERE tb.date = :date AND t.done = :done AND f.user.id = :userId")
    List<Task> findByDateAndUserId(LocalDate date, Long userId);
}