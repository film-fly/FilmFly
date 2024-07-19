package com.sparta.filmfly.domain.exam.repository;

import com.sparta.filmfly.domain.exam.entity.Exam;
import com.sparta.filmfly.global.exception.custom.exam.ExamCodeEnum;
import com.sparta.filmfly.global.exception.custom.exam.detail.ExamDetailCustomException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long>, ExamQueryRepository {

    default Exam findByIdOrElseThrow(Long examId) {
        return findById(examId)
            .orElseThrow(() -> new ExamDetailCustomException(ExamCodeEnum.EXAM_NOT_FOUND));
    }
}