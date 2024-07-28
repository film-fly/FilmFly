package com.sparta.filmfly.domain.comment.repository;

import com.sparta.filmfly.domain.comment.entity.Comment;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQueryRepository {
    default Comment findByIdOrElseThrow(Long commentId) {
        return findById(commentId)
                .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.COMMENT_NOT_FOUND));
    }
    Page<Comment> findByBoardId(Long boardId, Pageable pageable);
}