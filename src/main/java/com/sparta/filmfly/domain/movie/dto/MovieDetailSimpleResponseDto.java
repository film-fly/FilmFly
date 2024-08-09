package com.sparta.filmfly.domain.movie.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class MovieDetailSimpleResponseDto {
    private MovieSimpleResponseDto movie;
    private List<CreditSimpleResponseDto> credits;
    private MovieReactionCheckResponseDto reactions;
    private Long goodCount;
    private Long badCount;
    private Float avgRating;

    public void updateReaction(MovieReactionCheckResponseDto reactions) {
        this.reactions = reactions;
    }

    public void updateAvgRating(Float avgRating) {
        this.avgRating = avgRating;
    }
}