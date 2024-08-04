package com.sparta.filmfly.domain.report.repository;

import com.sparta.filmfly.domain.report.entity.Report;
import com.sparta.filmfly.domain.report.entity.ReportTypeEnum;
import com.sparta.filmfly.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByReporterIdAndReportedIdAndTypeIdAndType(User reporterId, User reportedId, Long typeId, ReportTypeEnum type);
}