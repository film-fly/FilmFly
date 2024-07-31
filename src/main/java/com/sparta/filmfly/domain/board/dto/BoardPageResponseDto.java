package com.sparta.filmfly.domain.board.dto;

import com.sparta.filmfly.domain.board.entity.Board;
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

    /**
     * content 제외한 기본 페이지 정보  정적 팩토리
     */
    public static BoardPageResponseDto fromDslPage(Page<BoardPageDto> boards) {
        return BoardPageResponseDto.builder()
                .totalPages(boards.getTotalPages())
                .totalElements(boards.getTotalElements())
                .currentPages(boards.getNumber() + 1)
                .size(boards.getSize())
                .content(boards.getContent())
                .build();
    }
}