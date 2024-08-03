package com.sparta.filmfly.domain.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BoardPageResponseDto {
    Long totalElements;
    int totalPages;
    int currentPage;
    int pageSize;
    List<BoardPageDto> content;
}