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

    @Value("${jwt_secret_key}")
    private String secretKey;
    private Key key;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

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

    private String createToken(String username, long expirationTime) {
        Date now = new Date();
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .setIssuedAt(now)
                .signWith(key, signatureAlgorithm);

        String token = builder.compact();
        // Base64 URL-safe encoding
//        String encodingToken = Base64.getUrlEncoder().encodeToString(token.getBytes());
//        log.info("encodingToken : {}", encodingToken);
        return token;
    }

    public String createAccessToken(String username) {
        long ACCESS_TOKEN_TIME = 30 * 60 * 1000L;
        return createToken(username, ACCESS_TOKEN_TIME);
    }

    public String createRefreshToken(String username) {
        long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L;
        return createToken(username, REFRESH_TOKEN_TIME);
    }

    public Claims getUserInfoFromToken(String token) {
//        String decodedToken = new String(Base64.getUrlDecoder().decode(token));
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token) {
        try {
//            String decodedToken = new String(Base64.getUrlDecoder().decode(token));
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
