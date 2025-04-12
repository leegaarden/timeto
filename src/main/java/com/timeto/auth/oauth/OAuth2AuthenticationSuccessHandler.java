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

            // 토큰 저장 페이지로 리다이렉트 (별도의 HTML 페이지를 사용)
            String saveTokenUrl = "/auth-success.html";
            String finalRedirectUrl = UriComponentsBuilder.fromUriString(saveTokenUrl)
                    .queryParam("token", token)
                    .queryParam("redirect", redirectUri)
                    .build().toUriString();

            log.info("토큰 저장 페이지로 리다이렉트: {}", finalRedirectUrl);
            getRedirectStrategy().sendRedirect(request, response, finalRedirectUrl);
        } catch (Exception e) {
            log.error("OAuth2 로그인 성공 처리 중 오류 발생", e);
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}