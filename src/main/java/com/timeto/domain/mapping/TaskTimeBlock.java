package com.timeto.domain.mapping;

import com.timeto.domain.BaseEntity;
import com.timeto.domain.Task;
import com.timeto.domain.TimeBlock;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_time_block")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskTimeBlock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_block_id", nullable = false)
    private TimeBlock timeBlock;

    @Builder
    public TaskTimeBlock(Task task, TimeBlock timeBlock) {
        this.task = task;
        this.timeBlock = timeBlock;
    }
}
