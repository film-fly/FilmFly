package com.sparta.filmfly.domain.review.service;

import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.movie.repository.MovieRepository;
import com.sparta.filmfly.domain.review.dto.ReviewCreateRequestDto;
import com.sparta.filmfly.domain.review.dto.ReviewResponseDto;
import com.sparta.filmfly.domain.review.entity.Review;
import com.sparta.filmfly.domain.review.repository.ReviewRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}