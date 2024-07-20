package com.sparta.filmfly.domain.board.dto;

import com.sparta.filmfly.domain.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardRequestDto {
    @NotBlank(message = "제목 입력하세요.")
    private String title;
    @NotBlank(message = "내용 입력하세요.")
    private String content;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .build();
    }
}