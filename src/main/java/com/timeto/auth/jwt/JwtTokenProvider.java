package com.timeto.auth.jwt;

import com.timeto.auth.oauth.CustomOAuth2User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:timeToSecretKeyForJwtAuthenticationTimeToSecretKeyForJwtAuthentication}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // 기본값 24시간
    private long jwtExpiration;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // JWT 토큰 생성
    public String generateToken(Authentication authentication) {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = customOAuth2User.getEmail();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 사용자 이메일 추출
    public String getEmailFromToken(String token) {
        // 토큰 유효성 검증
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("토큰이 비어있습니다.");
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            log.error("JWT 토큰에서 이메일 추출 실패: {}", e.getMessage());
            throw new RuntimeException("토큰에서 이메일을 추출할 수 없습니다: " + e.getMessage());
        }
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        // 토큰이 null이거나 빈 문자열인 경우
        if (!StringUtils.hasText(token)) {
            log.error("JWT 토큰이 비어있습니다");
            return false;
        }

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("JWT 토큰 유효성 검사 실패: {}", e.getMessage());
            return false;
        }
    }

    // Bearer 접두사 제거 및 토큰 추출
    public String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}