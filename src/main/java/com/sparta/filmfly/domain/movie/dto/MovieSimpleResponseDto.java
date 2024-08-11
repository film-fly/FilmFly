package com.sparta.filmfly.domain.movie.dto;

import com.sparta.filmfly.domain.movie.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class MovieSimpleResponseDto {
    private Long id;
    private String title;
    private String originalTitle;
    private String posterPath;
    private String backdropPath;
    private String overview;

    public static MovieSimpleResponseDto fromEntity(Movie movie) {
        return MovieSimpleResponseDto.builder()
            .id(movie.getId())
            .title(movie.getTitle())
            .originalTitle(movie.getOriginalTitle())
            .posterPath(movie.getPosterPath())
            .backdropPath(movie.getBackdropPath())
            .overview(movie.getOverview())
            .build();
    }
}