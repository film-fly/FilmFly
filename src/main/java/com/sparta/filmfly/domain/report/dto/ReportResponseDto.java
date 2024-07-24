package com.sparta.filmfly.domain.report.dto;

import com.sparta.filmfly.domain.report.entity.ReportTypeEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportResponseDto {
    private Long id;
    private Long reporterId;
    private Long reportedId;
    private String content;
    private Long typeId;
    private ReportTypeEnum type;
    private String reason;
    private String createdAt;
}
