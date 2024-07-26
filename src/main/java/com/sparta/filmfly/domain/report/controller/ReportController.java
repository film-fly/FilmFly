package com.sparta.filmfly.domain.report.controller;

import com.sparta.filmfly.domain.report.dto.ReportRequestDto;
import com.sparta.filmfly.domain.report.dto.ReportResponseDto;
import com.sparta.filmfly.domain.report.service.ReportService;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // 유저 신고
    @PostMapping
    public ResponseEntity<MessageResponseDto> reportUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Validated ReportRequestDto reportRequestDto
    ) {
        reportService.reportUser(userDetails.getUser().getId(), reportRequestDto);
        return ResponseUtils.success();
    }

    // 신고 목록 조회 (어드민 권한)
    @GetMapping
    public ResponseEntity<DataResponseDto<List<ReportResponseDto>>> getAllReports(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<ReportResponseDto> reports = reportService.getAllReports(userDetails.getUser());
        return ResponseUtils.success(reports);
    }
}
