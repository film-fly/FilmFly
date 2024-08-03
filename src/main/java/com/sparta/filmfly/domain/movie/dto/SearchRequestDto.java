package com.sparta.filmfly.domain.movie.dto;

import lombok.Getter;

@Getter
public class SearchRequestDto {
    private int page;
    private int size;
    private String sortBy;
    private boolean isAsc;

    private String keyword;
}
