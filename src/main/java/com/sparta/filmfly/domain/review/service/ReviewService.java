package com.sparta.filmfly.domain.review.service;

import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.movie.repository.MovieRepository;
import com.sparta.filmfly.domain.review.dto.ReviewCreateRequestDto;
import com.sparta.filmfly.domain.review.dto.ReviewResponseDto;
import com.sparta.filmfly.domain.review.dto.ReviewUpdateRequestDto;
import com.sparta.filmfly.domain.review.entity.Review;
import com.sparta.filmfly.domain.review.repository.ReviewRepository;
import com.sparta.filmfly.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;

    /**
     * 리뷰 저장
     */
    @Transactional
    public ReviewResponseDto createReview(User loginUser, ReviewCreateRequestDto requestDto) {
        Movie findMovie = movieRepository.findById(requestDto.getMovieId())
            .orElseThrow(() -> new IllegalArgumentException("영화 못 찾음")); // Movie 기능 올리면

        Review review = requestDto.toEntity(loginUser, findMovie);
        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.fromEntity(loginUser, savedReview);
    }

    /**
     * 리뷰 단일 조회
     */
    @Transactional(readOnly = true)
    public ReviewResponseDto getReview(Long reviewId) {
        Review findReview = reviewRepository.findByIdOrElseThrow(reviewId);

        return ReviewResponseDto.fromEntity(findReview.getUser(), findReview);
    }

    /**
     * 특정 영화에 대한 리뷰 전체 조회
     */
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getPageReview(Long movieId, Pageable pageable) {
        Page<Review> findReviews = reviewRepository.findByMovieId(movieId, pageable);
        List<ReviewResponseDto> responseDtos = new ArrayList<>();
        for (Review review : findReviews) {
            responseDtos.add(ReviewResponseDto.fromEntity(review.getUser(), review));
        }
        // QueryDSL 정렬 OrderSpecifier
        // QueryDSL 조건 BooleanExpression
        return responseDtos;
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public ReviewResponseDto updateReview(User loginUser, ReviewUpdateRequestDto requestDto, Long reviewId) {
        Review findReview = reviewRepository.findByIdOrElseThrow(reviewId);

        // 수정하려는 리뷰가 자기가 작성한 리뷰인지 확인
        findReview.checkReviewOwner(loginUser);

        findReview.updateReview(requestDto);
        return ReviewResponseDto.fromEntity(findReview.getUser(), findReview);
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(User loginUser, Long reviewId) {
        Review findReview = reviewRepository.findByIdOrElseThrow(reviewId);

        // 삭제하려는 리뷰가 자기가 작성한 리뷰인지 확인
        findReview.checkReviewOwner(loginUser);

        reviewRepository.delete(findReview);
    }
}