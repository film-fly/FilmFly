package com.sparta.filmfly.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserStatusEnum {
    VERIFIED("AVTIVE"), // 활성화 상태
    SUSPENDED("SUSPENDED"), // 정지된 상태
    DELETED("DELETED"); // 탈퇴한 상태

    private final String status;

    UserStatusEnum(String status) {
        this.status = status;
    }
}
