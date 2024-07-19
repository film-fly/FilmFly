package com.sparta.filmfly.domain.media.repository;

import com.sparta.filmfly.domain.media.entity.Media;
import com.sparta.filmfly.global.exception.custom.exam.ExamCodeEnum;
import com.sparta.filmfly.global.exception.custom.exam.detail.ExamDetailCustomException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository  extends JpaRepository<Media, Long> {
    default Media findByIdOrElseThrow(Long examId) {
        return findById(examId)
            .orElseThrow(() -> new ExamDetailCustomException(ExamCodeEnum.EXAM_NOT_FOUND));
    }
}