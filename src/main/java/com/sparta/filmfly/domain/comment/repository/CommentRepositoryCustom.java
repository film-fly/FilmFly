package com.sparta.filmfly.domain.comment.repository;

import com.sparta.filmfly.domain.comment.dto.CommentResponseDto;
import com.sparta.filmfly.global.common.response.PageResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom {
    PageResponseDto<List<CommentResponseDto>> findAllByBoardIdWithReactions(Long boardId, Pageable pageable);
    PageResponseDto<List<CommentResponseDto>> findAllByUserId(Long userId, Pageable pageable);
}