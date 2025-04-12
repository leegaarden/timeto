package com.timeto.auth.oauth;

import com.timeto.auth.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;

    @Value("${app.oauth2.redirect-uri:https://time-to.co.kr/goal}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        try {
            log.info("OAuth2 로그인 성공. 사용자: {}", ((CustomOAuth2User) authentication.getPrincipal()).getEmail());

            // JWT 토큰 생성
            String token = tokenProvider.generateToken(authentication);
            log.debug("JWT 토큰 생성 완료");

            // HTML로 응답 작성
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(
                    "<!DOCTYPE html>" +
                            "<html>" +
                            "<head>" +
                            "    <title>로그인 처리 중...</title>" +
                            "</head>" +
                            "<body>" +
                            "    <p>로그인 성공! 잠시만 기다려주세요...</p>" +
                            "    <script>" +
                            "        // 토큰을 localStorage에 저장" +
                            "        localStorage.setItem('token', '" + token + "');" +
                            "        console.log('토큰이 localStorage에 저장되었습니다.');" +
                            "        // 리다이렉트" +
                            "        window.location.href = '" + redirectUri + "';" +
                            "    </script>" +
                            "</body>" +
                            "</html>"
            );

            log.info("토큰 저장 스크립트 전송 완료. 리다이렉트 URL: {}", redirectUri);

        } catch (Exception e) {
            log.error("OAuth2 로그인 성공 처리 중 오류 발생", e);
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}