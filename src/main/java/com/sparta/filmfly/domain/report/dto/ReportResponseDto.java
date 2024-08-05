package com.sparta.filmfly.domain.report.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.filmfly.domain.report.entity.ReportTypeEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportResponseDto {
    private Long id;
    private Long reporterId;
    private String reporterNickname;
    private Long reportedId;
    private String reportedNickname;
    private String content;
    private Long typeId;
    private ReportTypeEnum type;
    private String reason;
    private String createdAt;
}
