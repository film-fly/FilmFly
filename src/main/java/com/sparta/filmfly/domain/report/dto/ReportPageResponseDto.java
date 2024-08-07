package com.sparta.filmfly.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReportPageResponseDto {
    private List<ReportResponseDto> reports;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}