package com.sparta.filmfly.domain.report.service;

import com.sparta.filmfly.domain.report.dto.ReportRequestDto;
import com.sparta.filmfly.domain.report.dto.ReportResponseDto;
import com.sparta.filmfly.domain.report.entity.Report;
import com.sparta.filmfly.domain.report.repository.ReportRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    /**
     * 유저 신고
     */
    @Transactional
    public void reportUser(Long reporterId, ReportRequestDto reportRequestDto) {
        User reporter = userRepository.findByIdOrElseThrow(reporterId);
        User reported = userRepository.findByIdOrElseThrow(reportRequestDto.getReportedId());

        // 중복 신고 확인
        reportRepository.checkIfAlreadyReported(reporter, reported, reportRequestDto.getTypeId(), reportRequestDto.getType());

        Report report = Report.builder()
                .reporterId(reporter)
                .reportedId(reported)
                .content(reportRequestDto.getContent())
                .typeId(reportRequestDto.getTypeId())
                .type(reportRequestDto.getType())
                .reason(reportRequestDto.getReason())
                .build();

        reportRepository.save(report);
    }

    /**
     * 신고 목록 조회 (어드민 권한)
     */
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getAllReports(User currentUser) {
        // 현재 사용자가 어드민인지 확인
        currentUser.validateAdminRole();

        List<Report> reports = reportRepository.findAll();
        return reports.stream()
                .map(report -> ReportResponseDto.builder()
                        .id(report.getId())
                        .reporterId(report.getReporterId().getId())
                        .reportedId(report.getReportedId().getId())
                        .content(report.getContent())
                        .typeId(report.getTypeId())
                        .type(report.getType())
                        .reason(report.getReason())
                        .createdAt(report.getCreatedAt().toString())
                        .build())
                .collect(Collectors.toList());
    }
}
