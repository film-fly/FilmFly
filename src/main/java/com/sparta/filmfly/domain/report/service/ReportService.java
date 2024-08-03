package com.sparta.filmfly.domain.report.service;

import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.domain.comment.repository.CommentRepository;
import com.sparta.filmfly.domain.report.dto.ReportPageResponseDto;
import com.sparta.filmfly.domain.report.dto.ReportRequestDto;
import com.sparta.filmfly.domain.report.dto.ReportResponseDto;
import com.sparta.filmfly.domain.report.entity.Report;
import com.sparta.filmfly.domain.report.entity.ReportTypeEnum;
import com.sparta.filmfly.domain.report.repository.ReportRepository;
import com.sparta.filmfly.domain.review.repository.ReviewRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.DuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 유저 신고
     */
    @Transactional
    public void reportUser(Long reporterId, ReportRequestDto reportRequestDto) {
        User reporter = userRepository.findByIdOrElseThrow(reporterId);
        User reported = userRepository.findByIdOrElseThrow(reportRequestDto.getReportedId());

        // 중복 신고 확인
        if (reportRepository.existsByReporterIdAndReportedIdAndTypeIdAndType(reporter, reported, reportRequestDto.getTypeId(), reportRequestDto.getType())) {
            throw new DuplicateException(ResponseCodeEnum.ALREADY_REPORTED);
        }

        // 원본 내용 가져오기
        String content = fetchContent(reportRequestDto.getType(), reportRequestDto.getTypeId());

        Report report = Report.builder()
                .reporterId(reporter)
                .reportedId(reported)
                .content(content)
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
    public ReportPageResponseDto getAllReports(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Report> reportsPage = reportRepository.findAll(pageable);

        List<ReportResponseDto> reportResponseDtos = reportsPage.getContent().stream()
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

        return ReportPageResponseDto.builder()
                .reports(reportResponseDtos)
                .totalElements(reportsPage.getTotalElements())
                .totalPages(reportsPage.getTotalPages())
                .currentPage(reportsPage.getNumber())
                .build();
    }

    /**
     * 원본 내용 가져오기
     */
    private String fetchContent(ReportTypeEnum type, Long typeId) {
        switch (type) {
            case BOARD:
                return boardRepository.findByIdOrElseThrow(typeId).getContent();
            case COMMENT:
                return commentRepository.findByIdOrElseThrow(typeId).getContent();
            case REVIEW:
                return reviewRepository.findByIdOrElseThrow(typeId).getContent();
            default:
                throw new IllegalArgumentException("Unknown report type: " + type);
        }
    }
}

