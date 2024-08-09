package com.sparta.filmfly.domain.movie.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MovieCreditResponseDto {

    private MovieSimpleResponseDto movie;
    private List<CreditSimpleResponseDto> credits;
}