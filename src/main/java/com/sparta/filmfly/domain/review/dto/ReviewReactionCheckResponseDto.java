package com.sparta.filmfly.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewReactionCheckResponseDto {

    private Boolean isGood;
    private Boolean isBad;
    private Boolean isBlock;
}