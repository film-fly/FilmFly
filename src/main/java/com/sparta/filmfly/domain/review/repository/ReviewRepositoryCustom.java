package com.sparta.filmfly.domain.review.repository;

import com.sparta.filmfly.domain.review.dto.ReviewResponseDto;
import com.sparta.filmfly.global.common.response.PageResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepositoryCustom {
    PageResponseDto<List<ReviewResponseDto>> getPageReviewByMovieId(Long movieId, Pageable pageable);
    PageResponseDto<List<ReviewResponseDto>> getPageReviewByUserId(Long userId, Pageable pageable);
    Float getAverageRatingByMovieId(Long movieId);
}