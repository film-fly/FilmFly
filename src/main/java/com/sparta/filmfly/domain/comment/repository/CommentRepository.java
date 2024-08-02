package com.sparta.filmfly.domain.comment.repository;

import com.sparta.filmfly.domain.comment.entity.Comment;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQueryRepository {
    default Comment findByIdOrElseThrow(Long commentId) {
        return findById(commentId)
                .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.COMMENT_NOT_FOUND));
    }
    default void existsByIdOrElseThrow(Long commentId) {
        if (!existsById(commentId)) {
            throw new NotFoundException(ResponseCodeEnum.COMMENT_NOT_FOUND);
        }
    }
}