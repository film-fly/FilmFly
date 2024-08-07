package com.sparta.filmfly.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserSearchPageResponseDto {
    private List<UserResponseDto> users;
    private long totalElements;
    private int currentPage;
    private int totalPages;
    private int pageSize;
}