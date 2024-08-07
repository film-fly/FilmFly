package com.sparta.filmfly.domain.reaction.dto;

import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.reaction.entity.Good;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReactionMovieResponseDto {

    private Long id;
    private Long movieId;
    private String title;
    private String originalTitle;
    private String posterPath;
    private String backdropPath;

    public static ReactionMovieResponseDto fromEntity(Good good, Movie movie) {
        return ReactionMovieResponseDto.builder()
            .id(good.getId())
            .movieId(movie.getId())
            .title(movie.getTitle())
            .originalTitle(movie.getOriginalTitle())
            .posterPath(movie.getPosterPath())
            .backdropPath(movie.getBackdropPath())
            .build();
    }
}