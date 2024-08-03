package com.sparta.filmfly.domain.review.repository;

import com.sparta.filmfly.domain.movie.dto.PageResponseDto;
import com.sparta.filmfly.domain.review.dto.ReviewResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    PageResponseDto<List<ReviewResponseDto>> getPageReviewByMovieId(Long movieId, Pageable pageable);
}