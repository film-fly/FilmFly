package com.sparta.filmfly.domain.user.dto;

import com.sparta.filmfly.domain.user.entity.UserRoleEnum;
import com.sparta.filmfly.domain.user.entity.UserStatusEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String introduce;
    private String pictureUrl;
    private UserRoleEnum userRole;
    private UserStatusEnum userStatus;
}
