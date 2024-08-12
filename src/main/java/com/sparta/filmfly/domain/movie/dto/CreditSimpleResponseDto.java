package com.sparta.filmfly.domain.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreditSimpleResponseDto {

    private Long id;
    private String name;
    private String originalName;
    private String profilePath;
}