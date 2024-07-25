package com.sparta.filmfly.domain.movie.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiCreditsResponse {
    private String id;
    private List<ApiCreditsResponseDto> cast;
    private List<Object> crew;
}
