package com.sparta.filmfly.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.filmfly.domain.user.entity.UserRoleEnum;
import com.sparta.filmfly.domain.user.entity.UserStatusEnum;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String introduce;
    private String pictureUrl;
    private UserRoleEnum userRole;
    private UserStatusEnum userStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
