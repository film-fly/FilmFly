package com.sparta.filmfly.domain.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class MovieReactionCheckResponseDto {

    private Boolean isGood;
    private Boolean isBad;
    private Boolean isFavorite;

    public static MovieReactionCheckResponseDto setupFalse() {
        return MovieReactionCheckResponseDto.builder()
            .isGood(false)
            .isBad(false)
            .isFavorite(false)
            .build();
    }
}