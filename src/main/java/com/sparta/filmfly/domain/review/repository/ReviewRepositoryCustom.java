package com.sparta.filmfly.domain.review.repository;

import com.sparta.filmfly.global.common.response.PageResponseDto;
import com.sparta.filmfly.domain.review.dto.ReviewResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    PageResponseDto<List<ReviewResponseDto>> getPageReviewByMovieId(Long movieId, Pageable pageable);
    Float getAverageRatingByMovieId(Long movieId);
}