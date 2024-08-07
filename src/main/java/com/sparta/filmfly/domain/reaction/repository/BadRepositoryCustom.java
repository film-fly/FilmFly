package com.sparta.filmfly.domain.reaction.repository;

import com.sparta.filmfly.domain.reaction.dto.ReactionBoardResponseDto;
import com.sparta.filmfly.domain.reaction.dto.ReactionCommentResponseDto;
import com.sparta.filmfly.domain.reaction.dto.ReactionMovieResponseDto;
import com.sparta.filmfly.domain.reaction.dto.ReactionReviewResponseDto;
import com.sparta.filmfly.global.common.response.PageResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BadRepositoryCustom {
    PageResponseDto<List<ReactionMovieResponseDto>> getPageMovieByUserBad(Long userId, Pageable pageable);

    PageResponseDto<List<ReactionReviewResponseDto>> getPageReviewByUserBad(Long userId, Pageable pageable);

    PageResponseDto<List<ReactionBoardResponseDto>> getPageBoardByUserBad(Long userId, Pageable pageable);

    PageResponseDto<List<ReactionCommentResponseDto>> getPageCommentByUserBad(Long userId, Pageable pageable);
}