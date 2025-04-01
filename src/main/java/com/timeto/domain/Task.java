package com.timeto.domain;

import com.timeto.domain.enums.Level;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "task")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Level level; // "HIGH", "MIDDLE", "LOW" 값을 가짐

    @Column(nullable = false)
    private LocalTime time; // 예상 소요 시간

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(nullable = false)
    private Boolean done;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Builder
    public Task(Folder folder, Level level, LocalTime time, String memo) {
        this.folder = folder;
        this.level = level;
        this.time = time;
        this.memo = memo;
        this.done = false;
    }
}