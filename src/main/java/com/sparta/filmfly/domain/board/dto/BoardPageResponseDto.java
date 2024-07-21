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
    List<BoardResponseDto> content;

    public static BoardPageResponseDto fromPage(Page<Board> boards) {
        //Page<BoardResponseDto> boardsDto = boards.map(BoardResponseDto::fromEntity);
        List<BoardResponseDto> boardsDto = boards.stream().map(BoardResponseDto::fromEntity).toList();

        return BoardPageResponseDto.builder()
                .totalPages(boards.getTotalPages())
                .totalElements(boards.getTotalElements())
                .currentPages(boards.getNumber()+1)
                .size(boards.getSize())
                .content(boardsDto)
                .build();
    }
}
