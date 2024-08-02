package com.sparta.filmfly.domain.movie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.filmfly.domain.movie.entity.Movie;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiMovieResponseDto {
    @JsonProperty("id")
    private int id;
    @JsonProperty("adult")
    private boolean adult;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("original_title")
    private String originalTitle;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("popularity")
    private double popularity;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("title")
    private String title;
    @JsonProperty("video")
    private boolean video;
    @JsonProperty("vote_average")
    private double voteAverage;
    @JsonProperty("vote_count")
    private int voteCount;

    public Movie toEntity() {
        return Movie.builder()
                .id((long) id)
                .adult(adult)
                .backdropPath(backdropPath)
                .genreIds(genreIds)
                .originalLanguage(originalLanguage)
                .originalTitle(originalTitle)
                .overview(overview)
                .popularity(popularity)
                .posterPath(posterPath)
                .releaseDate(releaseDate)
                .title(title)
                .video(video)
                .voteAverage(voteAverage)
                .voteCount(voteCount)
                .build();
    }

    public void updateEntity(Movie movie) {
        movie.update(
                adult,
                backdropPath,
                genreIds,
                originalLanguage,
                originalTitle,
                overview,
                popularity,
                posterPath,
                releaseDate,
                title,
                video,
                voteAverage,
                voteCount
        );
    }
}
