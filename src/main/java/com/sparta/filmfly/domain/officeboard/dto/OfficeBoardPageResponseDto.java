package com.sparta.filmfly.domain.officeboard.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class OfficeBoardPageResponseDto {
    Long totalElements;
    int totalPages;
    int currentPage;
    int pageSize;
    List<OfficeBoardResponseDto> content;
}
