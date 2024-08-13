package com.sparta.filmfly.domain.report.repository;

import com.sparta.filmfly.domain.report.entity.Report;
import com.sparta.filmfly.domain.report.entity.ReportTypeEnum;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.batch.hardDelete.SoftDeletableRepository;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportRepository extends JpaRepository<Report, Long>, SoftDeletableRepository<Report> {
    default Report findByIdOrElseThrow(Long reportId) {
        return findById(reportId)
                .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.REPORT_NOT_FOUND));
    }

    boolean existsByReporterIdAndReportedIdAndTypeIdAndType(User reporterId, User reportedId, Long typeId, ReportTypeEnum type);
}