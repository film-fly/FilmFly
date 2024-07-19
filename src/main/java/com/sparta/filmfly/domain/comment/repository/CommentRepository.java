package com.sparta.filmfly.domain.comment.repository;

import com.sparta.filmfly.domain.comment.entity.Comment;
import com.sparta.filmfly.global.exception.custom.exam.ExamCodeEnum;
import com.sparta.filmfly.global.exception.custom.exam.detail.ExamDetailCustomException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQueryRepository {
    default Comment findByIdOrElseThrow(Long examId) {
        return findById(examId)
            .orElseThrow(() -> new ExamDetailCustomException(ExamCodeEnum.EXAM_NOT_FOUND));
    }
}