package com.sparta.filmfly.domain.review.controller;

import com.sparta.filmfly.domain.review.dto.*;
import com.sparta.filmfly.domain.review.service.ReviewService;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.PageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import com.sparta.filmfly.global.util.PageUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 저장
     */
    @PostMapping("/movies/{movieId}/reviews")
    public ResponseEntity<DataResponseDto<ReviewResponseDto>> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long movieId,
            @Valid @RequestBody ReviewCreateRequestDto requestDto
    ) {
        ReviewResponseDto responseDto = reviewService.createReview(userDetails.getUser(), movieId, requestDto);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 리뷰 단일 조회
     */
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<DataResponseDto<ReviewResponseDto>> getReview(
            @PathVariable Long reviewId
    ) {
        ReviewResponseDto responseDto = reviewService.getReview(reviewId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 특정 영화에 대한 리뷰 전체 조회
     */
    @GetMapping("/movies/{movieId}/reviews")
    public ResponseEntity<DataResponseDto<PageResponseDto<List<ReviewResponseDto>>>> getPageReview(
            @PathVariable Long movieId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "false") boolean isAsc
    ) {
        Pageable pageable = PageUtils.of(page, size, sortBy, isAsc);
        PageResponseDto<List<ReviewResponseDto>> responseDto = reviewService.getPageReview(movieId, pageable);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 유저의 리뷰 목록
     */
    @GetMapping("/reviews/users/{userId}")
    public ResponseEntity<DataResponseDto<PageResponseDto<List<ReviewUserResponseDto>>>> getUsersReviews(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "false") boolean isAsc
    ) {
        Pageable pageable = PageUtils.of(page, size, sortBy, isAsc);
        PageResponseDto<List<ReviewUserResponseDto>> responseDto = reviewService.getUsersReviews(userId,pageable);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 리뷰 수정 권한 확인
     */
    @GetMapping("/reviews/{reviewId}/update-permission")
    public ResponseEntity<DataResponseDto<Boolean>> getReviewsUpdatePermission(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reviewId
    ) {
        Boolean response = reviewService.getReviewsUpdatePermission(userDetails.getUser(),reviewId);
        return ResponseUtils.success(response);
    }

    /**
     * 리뷰 수정
     */
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<DataResponseDto<ReviewUpdateResponseDto>> updateReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ReviewUpdateRequestDto requestDto,
            @PathVariable Long reviewId
    ) {
        ReviewUpdateResponseDto responseDto = reviewService.updateReview(userDetails.getUser(), requestDto, reviewId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<MessageResponseDto> deleteReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(userDetails.getUser(), reviewId);
        return ResponseUtils.success();
    }
}