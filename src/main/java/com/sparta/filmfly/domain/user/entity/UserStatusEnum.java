package com.sparta.filmfly.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserStatusEnum {
    UNVERIFIED("UNVERIFIED"), // 인증 전 상태
    VERIFIED("VERIFIED"), // 인증된 상태
    SUSPENDED("SUSPENDED"), // 정지된 상태
    DELETED("DELETED"); // 탈퇴한 상태

    private final String status;

    UserStatusEnum(String status) {
        this.status = status;
    }
}
