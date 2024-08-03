package com.sparta.filmfly.domain.comment.repository;

import com.sparta.filmfly.domain.comment.dto.CommentPageResponseDto;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    CommentPageResponseDto findAllByBoardIdWithReactions(Long boardId, Pageable pageable);
    CommentPageResponseDto findAllByUserId(Long userId, Pageable pageable);
}