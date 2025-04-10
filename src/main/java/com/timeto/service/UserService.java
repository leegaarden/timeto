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

    @Transactional
    public UserResponse.GetUserInfoRes getUserInfo (Long userId) {
        // 사용자 조회
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

        return new UserResponse.GetUserInfoRes(user.getId(), user.getName(), user.getEmail());
    }
}
