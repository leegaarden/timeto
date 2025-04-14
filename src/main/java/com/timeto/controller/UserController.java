package com.timeto.controller;


import com.timeto.apiPayload.ApiResponse;
import com.timeto.auth.jwt.JwtTokenProvider;
import com.timeto.config.exception.ErrorCode;
import com.timeto.config.exception.GeneralException;
import com.timeto.domain.User;
import com.timeto.dto.user.UserResponse;
import com.timeto.auth.oauth.CustomOAuth2User;
import com.timeto.repository.UserRepository;
import com.timeto.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "유저 API", description = "유저 관련 API")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @GetMapping
    @Operation(summary = "USER_API_01 : 유저 정보 조회", description = "유저의 정보 및 메뉴를 조회합니다.")
    public ApiResponse<UserResponse.GetUserInfoRes> getUserInfo (
            @RequestHeader("Authorization") String token
    ) {
        // 현재 인증된 사용자 정보 가져오기
        String email = jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED))
                .getId();

        UserResponse.GetUserInfoRes response = userService.getUserInfo(userId);

        return ApiResponse.success("유저 정보 조회에 성공했습니다.", response);
    }

    @DeleteMapping("/deactivate")
    @Operation(summary = "USER_API_02 : 회원 탈퇴", description = "현재 로그인한 사용자의 계정을 비활성화합니다.")
    public ApiResponse<?> deactivateAccount(
            @RequestHeader("Authorization") String token) {

        try {
            // 현재 인증된 사용자 정보 가져오기
            String email = jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new GeneralException(ErrorCode.USER_DEACTIVATED));

            // 이미 탈퇴한 회원인 경우
            if (!user.getActive()) {
                throw new GeneralException(ErrorCode.USER_ALREADY_DEACTIVATED);
            }

            // 회원 비활성화 처리
            user.deactivate();
            userRepository.save(user);

            return ApiResponse.success(null,"회원 탈퇴가 완료되었습니다.");

        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/login/oauth2/error")
    @Operation(summary = "USER_API_03 : 탈퇴한 회원이 재로그인 하는 경우 에러 처리", description = "현재 로그인한 사용자의 계정을 비활성화합니다.")
    public String handleOAuth2Error(@RequestParam("error_code") String errorCode, Model model) {
        if ("account_deactivated".equals(errorCode)) {
            model.addAttribute("errorMessage", "탈퇴한 계정입니다. 다른 이메일로 가입해주세요.");
            // 또는 계정 복구 옵션을 제공하는 페이지로 이동
        }
        return "oauth2-error";
    }
}
