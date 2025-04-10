package com.timeto.repository;

import com.timeto.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 찾기
    Optional<User> findByEmail(String email);

    // 이메일로 사용자 존재 여부 확인
    boolean existsByEmail(String email);

    // 활성 상태인 사용자를 이메일로 찾기
    Optional<User> findByEmailAndActive(String email, boolean active);

    // 활성 상태인 유저 조회
    Optional<User> findByIdAndActiveTrue(Long id);

}
