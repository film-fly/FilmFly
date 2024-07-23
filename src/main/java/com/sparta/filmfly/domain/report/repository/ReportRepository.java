package com.sparta.filmfly.domain.report.repository;

import com.sparta.filmfly.domain.report.entity.Report;
import com.sparta.filmfly.domain.report.entity.ReportTypeEnum;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.DuplicateException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByReporterAndReportedAndTypeIdAndType(User reporter, User reported, Long typeId, ReportTypeEnum type);

    default void checkIfAlreadyReported(User reporter, User reported, Long typeId, ReportTypeEnum type) {
        findByReporterAndReportedAndTypeIdAndType(reporter, reported, typeId, type)
                .ifPresent(report -> {
                    throw new DuplicateException(ResponseCodeEnum.ALREADY_REPORTED);
                });
    }
}
