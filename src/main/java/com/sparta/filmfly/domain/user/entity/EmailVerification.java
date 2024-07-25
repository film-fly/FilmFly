package com.sparta.filmfly.domain.user.entity;

import com.sparta.filmfly.global.common.TimeStampEntity;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.LimitedException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification extends TimeStampEntity {

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

    @Column(nullable = false)
    private int resendCount; // 재전송 횟수

    @Column(nullable = false)
    private LocalDateTime lastResendTime; // 마지막 재전송 시간

    // 인증 이메일 생성
    @Builder
    public EmailVerification(User user, String emailVerificationToken, LocalDateTime emailExpiryTime) {
        this.user = user;
        this.emailVerificationToken = emailVerificationToken;
        this.emailExpiryTime = emailExpiryTime;
        this.resendCount = 0;
        this.lastResendTime = LocalDateTime.now();
    }

    // 이메일 인증 토큰 업데이트
    public void updateEmailVerificationToken(String token) {
        this.emailVerificationToken = token;
    }

    // 이메일 만료 시간 설정
    public void createEmailExpiryTime(LocalDateTime expiryTime) {
        this.emailExpiryTime = expiryTime;
    }

    // 재전송 횟수 증가
    public void incrementResendCount() {
        this.resendCount++;
    }

    // 재전송 횟수 초기화
    public void resetResendCount() {
        this.resendCount = 0;
    }

    // 마지막 재전송 시간 업데이트
    public void updateLastResendTime(LocalDateTime time) {
        this.lastResendTime = time;
    }

    // 재전송 가능 여부 검증
    public void validateResendLimit() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(this.lastResendTime.plusHours(1))) {
            this.resendCount = 0; // 한 시간이 지났으면 재전송 횟수 초기화
        }

        if (this.resendCount >= 5) {
            throw new LimitedException(ResponseCodeEnum.EMAIL_RESEND_LIMIT);
        }
    }
}
