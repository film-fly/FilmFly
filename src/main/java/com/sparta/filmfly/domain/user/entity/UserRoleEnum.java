package com.sparta.filmfly.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    ROLE_USER("USER"),  // 사용자 권한
    ROLE_ADMIN("ADMIN");  // 관리자 권한

    private final String role;

    UserRoleEnum(String role) {
        this.role = role;
    }
}