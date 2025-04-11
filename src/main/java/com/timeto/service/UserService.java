package com.timeto.service;

import com.timeto.config.exception.ErrorCode;
import com.timeto.config.exception.GeneralException;
import com.timeto.domain.User;
import com.timeto.dto.user.UserResponse;
import com.timeto.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원 정보
    @Transactional
    public UserResponse.GetUserInfoRes getUserInfo (Long userId) {
        // 사용자 조회
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

        return new UserResponse.GetUserInfoRes(user.getId(), user.getName(), user.getEmail());
    }

    // 회원 탈퇴
    @Transactional
    public void deactivateUser(Long userId) {

        // 사용자 조회
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

        // 이미 탈퇴한 사용자인지 확인
        if (!user.getActive()) {
            throw new GeneralException(ErrorCode.USER_ALREADY_DEACTIVATED);
        }

        // active 상태를 false로 변경
        user.setActive(false);

        // 저장
        userRepository.save(user);
    }
}
