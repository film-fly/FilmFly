package com.sparta.filmfly.domain.report.service;

import com.sparta.filmfly.domain.report.dto.ReportRequestDto;
import com.sparta.filmfly.domain.report.entity.Report;
import com.sparta.filmfly.domain.report.repository.ReportRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    // 신고 하기
    @Transactional
    public void reportUser(Long reporterId, ReportRequestDto reportRequestDto) {
        User reporter = userRepository.findByIdOrElseThrow(reporterId);
        User reported = userRepository.findByIdOrElseThrow(reportRequestDto.getReportedId());

        // 중복 신고 확인
        reportRepository.checkIfAlreadyReported(reporter, reported, reportRequestDto.getTypeId(), reportRequestDto.getType());

        Report report = Report.builder()
                .reporter(reporter)
                .reported(reported)
                .content(reportRequestDto.getContent())
                .typeId(reportRequestDto.getTypeId())
                .type(reportRequestDto.getType())
                .reason(reportRequestDto.getReason())
                .build();

        reportRepository.save(report);
    }
}
