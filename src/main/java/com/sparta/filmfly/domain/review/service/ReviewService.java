package com.sparta.filmfly.domain.review.service;

import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.movie.repository.MovieRepository;
import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.repository.BadRepository;
import com.sparta.filmfly.domain.reaction.repository.GoodRepository;
import com.sparta.filmfly.domain.review.dto.*;
import com.sparta.filmfly.domain.review.entity.Review;
import com.sparta.filmfly.domain.review.repository.ReviewRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.response.PageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final GoodRepository goodRepository;
    private final BadRepository badRepository;

    /**
     * 리뷰 저장
     */
    @Transactional
    public ReviewResponseDto createReview(User loginUser, Long movieId, ReviewCreateRequestDto requestDto) {
        log.info("requestDto: {}", requestDto);
        Movie findMovie = movieRepository.findByIdOrElseThrow(movieId);

        Review review = requestDto.toEntity(loginUser, findMovie);
        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.fromEntity(savedReview);
    }

    /**
     * 리뷰 단일 조회
     */
    @Transactional(readOnly = true)
    public ReviewResponseDto getReview(Long reviewId) {
        Review findReview = reviewRepository.findByIdOrElseThrow(reviewId);

        Long goodCount = goodRepository.countByTypeAndTypeId(ReactionContentTypeEnum.REVIEW, reviewId);
        Long badCount = badRepository.countByTypeAndTypeId(ReactionContentTypeEnum.REVIEW, reviewId);
        return ReviewResponseDto.fromEntity(findReview, goodCount, badCount);
    }

    /**
     * 특정 영화에 대한 리뷰 전체 조회
     */
    @Transactional(readOnly = true)
    public PageResponseDto<List<ReviewResponseDto>> getPageReview(Long movieId, Pageable pageable) {
        return reviewRepository.getPageReviewByMovieId(movieId, pageable);
    }

    /**
     * 유저의 리뷰 목록
     */
    @Transactional(readOnly = true)
    public PageResponseDto<List<ReviewUserResponseDto>> getUsersReviews(Long userId, Pageable pageable) {
        return reviewRepository.getPageReviewByUserId(userId, pageable);
    }

    /**
     * 리뷰 수정 권한 확인
     */
    public Boolean getReviewsUpdatePermission(User user, Long reviewId) {
        Review review = reviewRepository.findByIdOrElseThrow(reviewId);
        //admin이면 true 반환
        if(!user.isAdmin()) {
            review.checkReviewOwner(user);
        }
        return true; //수정 권한 없으면 에러?
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public ReviewUpdateResponseDto updateReview(User loginUser, ReviewUpdateRequestDto requestDto, Long reviewId) {
        log.info("requestDto: {}", requestDto);
        Review findReview = reviewRepository.findByIdOrElseThrow(reviewId);

        // 수정하려는 리뷰가 자기가 작성한 리뷰인지 확인
        findReview.checkReviewOwner(loginUser);

        findReview.updateReview(requestDto);
        return ReviewUpdateResponseDto.fromEntity(findReview);
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(User loginUser, Long reviewId) {
        Review findReview = reviewRepository.findByIdOrElseThrow(reviewId);

        // 삭제하려는 리뷰가 자기가 작성한 리뷰인지 확인
        if(!loginUser.isAdmin()) { //관리자는 삭제 가능
            findReview.checkReviewOwner(loginUser);
        }
        reviewRepository.delete(findReview);
    }

    /**
     * 최신 리뷰 목록
     */
    @Transactional(readOnly = true)
    public PageResponseDto<List<ReviewUserResponseDto>> getReviews(Pageable pageable) {
        return reviewRepository.getPageReview(pageable);
    }
}