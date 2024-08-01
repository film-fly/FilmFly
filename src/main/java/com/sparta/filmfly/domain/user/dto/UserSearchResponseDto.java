package com.sparta.filmfly.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserSearchResponseDto {
    private List<UserResponseDto> users;
    private long userCount;
    private int currentPage;
    private int totalPages;
}
