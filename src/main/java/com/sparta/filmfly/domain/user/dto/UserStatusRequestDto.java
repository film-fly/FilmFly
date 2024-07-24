package com.sparta.filmfly.domain.user.dto;

import com.sparta.filmfly.domain.user.entity.UserStatusEnum;
import lombok.Getter;

@Getter
public class UserStatusRequestDto {
    private UserStatusEnum status;
}
