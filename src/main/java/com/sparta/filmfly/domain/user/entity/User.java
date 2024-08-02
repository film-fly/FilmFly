package com.sparta.filmfly.domain.user.entity;

import com.sparta.filmfly.global.common.TimeStampEntity;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class User extends TimeStampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
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

    @Column
    private LocalDateTime deletedAt;


    /**
     * 사용자 생성
     */
    @Builder
    public User(String username, String password, String email, String nickname, Long kakaoId, String pictureUrl, UserStatusEnum userStatus, UserRoleEnum userRole) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.kakaoId = kakaoId;
        this.pictureUrl = pictureUrl;
        this.userStatus = userStatus;
        this.userRole = userRole;
    }

    /**
     * 탈퇴 상태 검증
     */
    public void validateDeletedStatus() {
        if (this.userStatus == UserStatusEnum.DELETED) {
            throw new DeletedException(ResponseCodeEnum.USER_DELETED);
        }
    }

    /**
     * 정지 상태 검증
     */
    public void validateSuspendedStatus() {
        if (this.userStatus == UserStatusEnum.SUSPENDED) {
            throw new SuspendedException(ResponseCodeEnum.USER_SUSPENDED);
        }
    }

    /**
     * 비밀번호 검증
     */
    public void validatePassword(String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, this.password)) {
            throw new InformationMismatchException(ResponseCodeEnum.PASSWORD_INCORRECT);
        }
    }

    /**
     * 새로운 비밀번호 검증
     */
    public void validateNewPassword(String newPassword, PasswordEncoder passwordEncoder) {
        if (passwordEncoder.matches(newPassword, this.password)) {
            throw new DuplicateException(ResponseCodeEnum.SAME_AS_OLD_PASSWORD);
        }
    }


    /**
     * 리프레시 토큰 업데이트
     */
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * 리프레시 토큰 삭제
     */
    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

    /**
     * 인증된 상태로 변경
     */
    public void updateVerified() {
        this.userStatus = UserStatusEnum.VERIFIED;
    }

    /**
     * 유저 정지 상태로 변경
     */
    public void updateSuspended() {
        this.userStatus = UserStatusEnum.SUSPENDED;
    }

    /**
     * 유저 탈퇴 상태로 변경
     */
    public void updateDeleted() {
        this.userStatus = UserStatusEnum.DELETED;
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * 비밀번호 업데이트
     */
    public void updatePassword(String password) {
        this.password = password;
    }

    /**
     * 프로필 업데이트
     */
    public void updateProfile(String nickname, String introduce, String pictureUrl) {
        this.nickname = nickname;
        this.introduce = introduce;
        this.pictureUrl = pictureUrl;
    }
}
