package com.timeto.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @Builder
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.active = true;
    }
}