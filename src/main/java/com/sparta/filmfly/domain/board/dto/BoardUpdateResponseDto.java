package com.sparta.filmfly.domain.board.dto;

import com.sparta.filmfly.domain.board.entity.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardUpdateResponseDto {
    private Long id;
    private String title;
    private String content;

    public static BoardUpdateResponseDto fromEntity(Board board) {
        return BoardUpdateResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }
}
