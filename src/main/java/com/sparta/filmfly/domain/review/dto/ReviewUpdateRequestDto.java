package com.sparta.filmfly.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReviewUpdateRequestDto {

    @NotBlank(message = "제목을 입력해 주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;
    @NotNull(message = "별점을 입력해 주세요.")
    private Float rating;
}