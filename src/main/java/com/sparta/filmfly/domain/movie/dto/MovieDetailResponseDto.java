package com.sparta.filmfly.domain.movie.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MovieDetailResponseDto {
    // 영화 응답
    private MovieResponseDto movie;
    // 배우 응답
    private List<CreditResponseDto> creditList;

}
