package com.sparta.filmfly.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardRequestDto {
    @NotBlank(message = "제목 입력하세요.")
    private String title;
    @NotBlank(message = "내용 입력하세요.")
    private String content;
}