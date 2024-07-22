package com.sparta.filmfly.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserStatusResponseDto {
    private List<UserResponseDto> users;
    private long userCount;
}
