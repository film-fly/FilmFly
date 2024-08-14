package com.sparta.filmfly.domain.review.dto;

import com.sparta.filmfly.domain.movie.dto.MovieReactionCheckResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewReactionCheckResponseDto {
    private Long id;
    private Boolean isGood;
    private Boolean isBad;
    private Boolean isBlock;

    public static ReviewReactionCheckResponseDto setupFalse() {
        return ReviewReactionCheckResponseDto.builder()
            .isGood(false)
            .isBad(false)
            .isBlock(false)
            .build();
    }
}