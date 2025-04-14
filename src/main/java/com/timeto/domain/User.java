package com.timeto.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(length = 15, nullable = false)
    private String name;

    @Setter
    @Column(length = 225, nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private Boolean active;

    // 탈퇴 시간 추가
    @Setter
    @Column(nullable = true)
    private LocalDateTime deactivatedAt;

    @Builder
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.active = true;
    }

    // 탈퇴 처리 메서드
    public void deactivate() {
        this.active = false;
        this.deactivatedAt = LocalDateTime.now();
    }
}