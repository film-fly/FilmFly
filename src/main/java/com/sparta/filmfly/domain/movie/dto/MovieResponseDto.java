package com.sparta.filmfly.domain.movie.dto;

import com.sparta.filmfly.domain.movie.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MovieResponseDto {
    private String movieTitle;

    public static MovieResponseDto fromEntity(Movie movie) {
        return MovieResponseDto.builder()
                .movieTitle(movie.getTitle())
                .build();
    }
}