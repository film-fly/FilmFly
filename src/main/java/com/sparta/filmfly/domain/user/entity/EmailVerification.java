package com.sparta.filmfly.domain.user.entity;

import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String emailVerificationToken;

    private LocalDateTime emailExpiryTime;

    private boolean verified;

    private int sendCount;

    private LocalDateTime lastResendTime;

    @Builder
    public EmailVerification(String email) {
        this.email = email;
        this.emailVerificationToken = generateVerificationToken();
        this.emailExpiryTime = LocalDateTime.now().plusMinutes(3);
        this.verified = false;
        this.sendCount = 0;
        this.lastResendTime = LocalDateTime.now();
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public void updateEmailVerificationToken() {
        this.emailVerificationToken = generateVerificationToken();
        this.emailExpiryTime = LocalDateTime.now().plusMinutes(3);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.emailExpiryTime);
    }

    public void validateSendLimit() {
        if (this.sendCount >= 5) {
            throw new LimitedException(ResponseCodeEnum.EMAIL_RESEND_LIMIT);
        }
    }

    public void incrementSendCount() {
        this.sendCount++;
    }

    public void updateLastResendTime(LocalDateTime resendTime) {
        this.lastResendTime = resendTime;
    }

    public void validateToken(String token) {
        if (!this.emailVerificationToken.equals(token)) {
            throw new InformationMismatchException(ResponseCodeEnum.EMAIL_VERIFICATION_TOKEN_MISMATCH);
        }
    }

    public void validateExpiryTime() {
        if (isExpired()) {
            throw new ExpiredException(ResponseCodeEnum.EMAIL_VERIFICATION_EXPIRED);
        }
    }

    public void verify() {
        this.verified = true;
    }

    public void validateVerifiedStatus() {
        if (!this.verified) {
            throw new AccessDeniedException(ResponseCodeEnum.EMAIL_VERIFICATION_REQUIRED);
        }
    }
}