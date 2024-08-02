package com.sparta.filmfly.domain.movie.dto;

import com.sparta.filmfly.global.common.response.DataResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class PageResponseDto<T> {
    private T data;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
}
