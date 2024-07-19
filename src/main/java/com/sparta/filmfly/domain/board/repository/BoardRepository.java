package com.sparta.filmfly.domain.board.repository;

import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.global.exception.custom.exam.ExamCodeEnum;
import com.sparta.filmfly.global.exception.custom.exam.detail.ExamDetailCustomException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    default Board findByIdOrElseThrow(Long examId) {
        return findById(examId)
            .orElseThrow(() -> new ExamDetailCustomException(ExamCodeEnum.EXAM_NOT_FOUND));
    }
}