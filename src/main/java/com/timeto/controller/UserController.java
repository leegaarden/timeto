package com.timeto.controller;


import com.timeto.apiPayload.ApiResponse;
import com.timeto.dto.user.UserResponse;
import com.timeto.oauth.CustomOAuth2User;
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
    @GetMapping
    @Operation(summary = "USER_API_01 : 유저 정보 조회", description = "유저의 정보 및 메뉴를 조회합니다.")
    public ApiResponse<UserResponse.GetUserInfoRes> getUserInfo (
            Authentication authentication
    ) {
        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        UserResponse.GetUserInfoRes response = userService.getUserInfo(userId);

        return ApiResponse.success("유저 정보 조회에 성공했습니다.", response);
    }

    @PostMapping("/logout")
    @Operation(summary = "USER_API_02 : 로그아웃", description = "현재 로그인한 사용자의 세션을 종료합니다.")
    public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 현재 세션 가져오기
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

        // Spring Security 컨텍스트에서 인증 정보 제거
        SecurityContextHolder.clearContext();

        // 세션 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ApiResponse.success("로그아웃에 성공했습니다.", null);
    }

    @DeleteMapping("/deactivate")
    @Operation(summary = "USER_API_03 : 회원 탈퇴", description = "현재 로그인한 사용자의 계정을 비활성화합니다.")
    public ApiResponse<Void> deactivateUser(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authentication) {

        // 현재 인증된 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getId();

        // 회원 탈퇴 처리
        userService.deactivateUser(userId);

        // 로그아웃 처리
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.clearContext();

        // 세션 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ApiResponse.success("회원 탈퇴에 성공했습니다.", null);
    }

    @GetMapping("/login/oauth2/error")
    @Operation(summary = "USER_API_04 : 탈퇴한 회원이 재로그인 하는 경우 에러 처리", description = "현재 로그인한 사용자의 계정을 비활성화합니다.")
    public String handleOAuth2Error(@RequestParam("error_code") String errorCode, Model model) {
        if ("account_deactivated".equals(errorCode)) {
            model.addAttribute("errorMessage", "탈퇴한 계정입니다. 다른 이메일로 가입해주세요.");
            // 또는 계정 복구 옵션을 제공하는 페이지로 이동
        }
        return "oauth2-error";
    }
}
