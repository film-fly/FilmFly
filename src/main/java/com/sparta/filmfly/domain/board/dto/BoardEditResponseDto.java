package com.sparta.filmfly.domain.board.dto;

import com.sparta.filmfly.domain.board.entity.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardEditResponseDto {
    private Long id;
    private String title;
    private String content;

    public static BoardEditResponseDto fromEntity(Board board) {
        return BoardEditResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }
}
