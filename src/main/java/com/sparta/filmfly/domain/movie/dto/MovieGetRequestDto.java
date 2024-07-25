package com.sparta.filmfly.domain.movie.dto;

import com.sparta.filmfly.domain.movie.entity.MovieSearchTypeEnum;
import lombok.Getter;

@Getter
public class MovieGetRequestDto {
    private int page;
    private int size;
    private String sortBy;
    private boolean isAsc;

    private MovieSearchTypeEnum searchType;  // 검색 타입 : 제목, 배우, 제작사

    private String keyword;

}
