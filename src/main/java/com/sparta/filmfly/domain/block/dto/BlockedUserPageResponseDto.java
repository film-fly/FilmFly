package com.sparta.filmfly.domain.block.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BlockedUserPageResponseDto {
    private List<BlockedUserResponseDto> users;
    private long totalElements;
    private int currentPage;
    private int totalPages;
    private int pageSize;
}