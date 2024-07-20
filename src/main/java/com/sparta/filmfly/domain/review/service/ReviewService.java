package com.sparta.filmfly.domain.review.service;

import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.movie.repository.MovieRepository;
import com.sparta.filmfly.domain.review.dto.ReviewCreateRequestDto;
import com.sparta.filmfly.domain.review.dto.ReviewResponseDto;
import com.sparta.filmfly.domain.review.entity.Review;
import com.sparta.filmfly.domain.review.repository.ReviewRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
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

    private final UserRepository userRepository; // 로그인 기능 완료되면 없애기
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public ReviewResponseDto saveReview(ReviewCreateRequestDto requestDto) {
        Movie findMovie = movieRepository.findById(requestDto.getMovieId())
            .orElseThrow(() -> new IllegalArgumentException("영화 못 찾음")); // Movie 기능 올리면

        User findUser = userRepository.findByIdOrElseThrow(1L); // 로그인 기능 완료되면 없애기
        Review review = requestDto.toEntity(findUser, findMovie);
        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.fromEntity(findUser, savedReview);
    }

    @Transactional(readOnly = true)
    public ReviewResponseDto findReview(Long reviewId) {
        User findUser = userRepository.findByIdOrElseThrow(1L); // 로그인 기능 완료되면 없애기
        Review findReview = reviewRepository.findByIdOrElseThrow(reviewId);

        return ReviewResponseDto.fromEntity(findUser, findReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findReviews(Long movieId, Pageable pageable) {
        Page<Review> findReviews = reviewRepository.findByMovieId(movieId, pageable);
        List<ReviewResponseDto> responseDtos = new ArrayList<>();
        for (Review review : findReviews) {
            responseDtos.add(ReviewResponseDto.fromEntity(review.getUser(), review));
        }
        // QueryDSL 정렬 OrderSpecifier
        // QueryDSL 조건 BooleanExpression
        return responseDtos;
    }
}