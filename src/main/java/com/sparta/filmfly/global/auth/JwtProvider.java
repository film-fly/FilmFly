package com.sparta.filmfly.global.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String USER_ID_CLAIM = "userId";

    @Value("${jwt_secret_key}")
    private String secretKey;
    private Key key;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /**
     * 비밀 키 초기화
     */
    @PostConstruct
    public void initializeSecretKey() {
        try {
            byte[] bytes = Base64.getDecoder().decode(secretKey);
            key = Keys.hmacShaKeyFor(bytes);
            log.debug("Secret key initialized successfully.");
        } catch (IllegalArgumentException e) {
            log.error("Failed to decode Base64 secret key: {}", e.getMessage());
            throw new IllegalArgumentException("Base64 비밀 키 디코딩 실패: " + e.getMessage(), e);
        }
    }

    /**
     * JWT 토큰 생성
     */
    private String createToken(String username, Long userId, long expirationTime) {
        Date now = new Date();
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .claim(USER_ID_CLAIM, userId)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .setIssuedAt(now)
                .signWith(key, signatureAlgorithm);

        return builder.compact();
    }

    /**
     * 액세스 토큰 생성
     */
    public String createAccessToken(String username, Long userId) {
        long ACCESS_TOKEN_TIME = 30 * 60 * 1000L;
        return createToken(username, userId, ACCESS_TOKEN_TIME);
    }

    /**
     * 리프레시 토큰 생성
     */
    public String createRefreshToken(String username, Long userId) {
        long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L;
        return createToken(username, userId, REFRESH_TOKEN_TIME);
    }

    /**
     * JWT 토큰에서 사용자 정보 추출
     */
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /**
     * JWT 토큰 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않는 JWT 서명 또는 잘못된 토큰 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰 입니다.");
        }
        return false;
    }
}
