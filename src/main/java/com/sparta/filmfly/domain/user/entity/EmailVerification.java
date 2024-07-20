package com.sparta.filmfly.domain.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String emailVerificationToken; // 인증 토큰

    @Column(nullable = false)
    private LocalDateTime emailExpiryTime; // 인증 토큰 만료 시간

    // 인증 이메일 생성
    @Builder
    public EmailVerification(User user, String emailVerificationToken, LocalDateTime emailExpiryTime) {
        this.user = user;
        this.emailVerificationToken = emailVerificationToken;
        this.emailExpiryTime = emailExpiryTime;
    }

    // 이메일 인증 토큰 업데이트
    public void updateEmailVerificationToken(String token) {
        this.emailVerificationToken = token;
    }

    // 이메일 만료 시간 설정
    public void createEmailExpiryTime(LocalDateTime expiryTime) {
        this.emailExpiryTime = expiryTime;
    }
}
