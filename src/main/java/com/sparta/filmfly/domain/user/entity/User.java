package com.sparta.filmfly.domain.user.entity;

import com.sparta.filmfly.global.common.TimeStampEntity;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.AccessDeniedException;
import com.sparta.filmfly.global.exception.custom.detail.InformationMismatchException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP where id = ?")
public class User extends TimeStampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatusEnum userStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleEnum userRole;

    @Column
    private String introduce;

    @Column
    private String pictureUrl;

    @Column
    private String refreshToken;

    @Column
    private Long kakaoId;

    // 일반 회원가입 유저 생성
    @Builder
    public User(String username, String password, String email, UserStatusEnum userStatus, UserRoleEnum userRole) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userStatus = userStatus;
        this.userRole = userRole;
    }

    // 사용자가 탈퇴,정지,이메일 인증안된 상태인지 검증
    public void validateUserStatus() {
        if (this.userStatus == UserStatusEnum.DELETED) {
            throw new AccessDeniedException(ResponseCodeEnum.USER_DELETED);
        } else if (this.userStatus == UserStatusEnum.SUSPENDED) {
            throw new AccessDeniedException(ResponseCodeEnum.USER_SUSPENDED);
        } else if (this.userStatus == UserStatusEnum.UNVERIFIED){
            throw new AccessDeniedException(ResponseCodeEnum.EMAIL_VERIFICATION_REQUIRED);
        }
    }

    // 비밀번호가 일치하는지 검증
    public void validatePassword(String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, this.password)) {
            throw new InformationMismatchException(ResponseCodeEnum.PASSWORD_INCORRECT);
        }
    }

    // 리프레시 토큰 업데이트
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    // 인증된 상태로 변경하는 메서드
    public void updateVerified() {
        if (this.userStatus != UserStatusEnum.UNVERIFIED) {
            throw new IllegalStateException("User is already verified or in an invalid state.");
        }
        this.userStatus = UserStatusEnum.VERIFIED;
    }
}

