package com.sparta.filmfly.domain.review.repository;

import com.sparta.filmfly.domain.review.entity.Review;
import com.sparta.filmfly.global.common.batch.hardDelete.SoftDeletableRepository;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom, SoftDeletableRepository<Review> {

    default Review findByIdOrElseThrow(Long reviewId) {
        return findById(reviewId)
            .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.REVIEW_NOT_FOUND));
    }

    List<Review> findByMovieId(Long movieId);
    Page<Review> findByMovieId(Long movieId, Pageable pageable);
}