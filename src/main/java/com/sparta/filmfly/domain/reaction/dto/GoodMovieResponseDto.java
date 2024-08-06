package com.sparta.filmfly.domain.reaction.dto;

import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.reaction.entity.Good;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoodMovieResponseDto {

    private Long id;
    private Long movieId;
    private String title;
    private String posterPath;

    public static GoodMovieResponseDto fromEntity(Good good, Movie movie) {
        return GoodMovieResponseDto.builder()
            .id(good.getId())
            .movieId(movie.getId())
            .title(movie.getTitle())
            .posterPath(movie.getPosterPath())
            .build();
    }
}