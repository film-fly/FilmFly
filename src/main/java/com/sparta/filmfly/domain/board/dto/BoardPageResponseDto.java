package com.sparta.filmfly.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class BoardPageResponseDto {
    int totalPages;
    Long totalElements;
    int currentPages;
    int size;
    List<BoardPageDto> content;
}