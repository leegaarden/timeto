package com.timeto.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "folder")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Folder extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    @Column(length = 25, nullable = false)
    private String name;

    @Column(name = "display_order")
    private Integer displayOrder;

    // 폴더가 삭제되면 할 일도 삭제됨
    @OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE)
    private List<Task> tasks;

    public void updateName(String name) {
        this.name = name;
    }

    public void updateOrder(Integer order) {
        this.displayOrder = order;
    }

    @Builder
    public Folder(Goal goal, String name, Integer displayOrder) {
        this.goal = goal;
        this.name = name;
        this.displayOrder = displayOrder;
    }
}