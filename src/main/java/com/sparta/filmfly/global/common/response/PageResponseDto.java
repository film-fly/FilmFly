package com.sparta.filmfly.global.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageResponseDto<T> {
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private T data;
}