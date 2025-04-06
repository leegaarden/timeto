package com.timeto.domain;

import com.timeto.domain.enums.Color;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "goal")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goal extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 25, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Color color;

    // 목표가 삭제되면 폴더도 삭제됨
    @OneToMany(mappedBy = "goal", cascade = CascadeType.REMOVE)
    private List<Folder> folders;

    @Builder
    public Goal(User user, String name, Color color) {
        this.user = user;
        this.name = name;
        this.color = color;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = Color.valueOf(color);
    }
}