package com.sparta.filmfly.domain.board.dto;

import com.sparta.filmfly.domain.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardResponseDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public static BoardResponseDto fromEntity(Board board) {
        return BoardResponseDto.builder()
            .title(board.getTitle())
            .content(board.getContent())
            .build();
    }

}