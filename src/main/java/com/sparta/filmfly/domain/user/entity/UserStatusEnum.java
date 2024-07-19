package com.sparta.filmfly.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserStatusEnum {
    UNVERIFIED("UNVERIFIED"), // 인증 전 상태
    VERIFIED("VERIFIED"), // 인증된 상태
    RESTRICTED("RESTRICTED"), // 특정 기능을 못하게 막는 상태
    DELETED("DELETED"); // 탈퇴한 상태

    private final String status;

    UserStatusEnum(String status) {
        this.status = status;
    }
}