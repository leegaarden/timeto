package com.timeto.domain;

import com.timeto.domain.enums.Level;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "task")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25, nullable = false)
    @Setter
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private Level level; // "HIGH", "MIDDLE", "LOW" 값을 가짐

    @Column(nullable = false)
    @Setter
    private LocalTime time; // 예상 소요 시간

    @Column(columnDefinition = "TEXT", nullable = true)
    private String memo;

    @Column(nullable = false)
    private Boolean done;

    @Column(name = "display_order")
    private Integer displayOrder;

    // 할 일이 삭제되면 타임블록 연결도 삭제됨
    // 타임블록과 일대일 관계
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "time_block_id")
    @Setter
    private TimeBlock timeBlock;

    public void updateName(String name) {
        this.name = name;
    }

    public void updateTime(LocalTime time) {
        this.time = time;
    }

    public void updateLevel(Level level) {
        this.level = level;
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }

    public void updateDone() { this.done = true; }

    public void updateOrder(Integer order) { this.displayOrder = order; }

    @Builder
    public Task(Folder folder, String name, Level level, LocalTime time, String memo, Boolean done, Integer displayOrder, TimeBlock timeBlock) {
        this.folder = folder;
        this.name = name;
        this.level = level;
        this.time = time;
        this.memo = memo;
        this.done = done != null ? done : false;
        this.displayOrder = displayOrder;
        this.timeBlock = timeBlock;
    }
}