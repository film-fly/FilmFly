package com.sparta.filmfly.domain.movie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.filmfly.domain.movie.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MovieResponseDto {
    private Long id;
    private boolean adult;
    private String backdropPath;
    private List<Integer> genreIds;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private double popularity;
    private String posterPath;
    private String releaseDate;
    private String title;

    public static MovieResponseDto fromEntity(Movie movie) {
        return MovieResponseDto.builder()
                .id(movie.getId())
                .adult(movie.isAdult())
                .backdropPath(movie.getBackdropPath())
                .genreIds(movie.getGenreIds())
                .originalLanguage(movie.getOriginalLanguage())
                .originalTitle(movie.getOriginalTitle())
                .overview(movie.getOverview())
                .popularity(movie.getPopularity())
                .posterPath(movie.getPosterPath())
                .releaseDate(movie.getReleaseDate())
                .title(movie.getTitle())
                .build();
    }
}