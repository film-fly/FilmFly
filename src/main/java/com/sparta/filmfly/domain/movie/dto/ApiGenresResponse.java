package com.sparta.filmfly.domain.movie.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ApiGenresResponse {
    private List<GenresResponseDto> genres;
}
