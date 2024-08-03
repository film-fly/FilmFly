package com.sparta.filmfly.domain.movie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiMovieResponse {
    @JsonProperty("page")
    private int page;
    @JsonProperty("results")
    private List<ApiMovieResponseDto> results;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("total_results")
    private int totalResults;
}
