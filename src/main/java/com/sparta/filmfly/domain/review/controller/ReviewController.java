package com.sparta.filmfly.domain.review.controller;

import com.sparta.filmfly.domain.review.dto.ReviewCreateRequestDto;
import com.sparta.filmfly.domain.review.dto.ReviewResponseDto;
import com.sparta.filmfly.domain.review.dto.ReviewUpdateRequestDto;
import com.sparta.filmfly.domain.review.service.ReviewService;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import com.sparta.filmfly.global.util.PageUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 저장
     */
    @PostMapping("/{movieId}/reviews")
    public ResponseEntity<DataResponseDto<ReviewResponseDto>> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long movieId,
            @Valid @RequestBody ReviewCreateRequestDto requestDto
    ) {
        ReviewResponseDto responseDto = reviewService.createReview(userDetails.getUser(),
                requestDto);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 리뷰 단일 조회
     */
    @GetMapping("/{movieId}/reviews/{reviewId}")
    public ResponseEntity<DataResponseDto<ReviewResponseDto>> getReview(
            @PathVariable Long movieId,
            @PathVariable Long reviewId
    ) {
        ReviewResponseDto responseDto = reviewService.getReview(reviewId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 특정 영화에 대한 리뷰 전체 조회
     */
    @GetMapping("/{movieId}/reviews")
    public ResponseEntity<DataResponseDto<List<ReviewResponseDto>>> getPageReview(
            @PathVariable Long movieId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "false") boolean isAsc
    ) {
        Pageable pageable = PageUtils.of(page, size, sortBy, isAsc);
        List<ReviewResponseDto> responseDtos = reviewService.getPageReview(movieId, pageable);
        return ResponseUtils.success(responseDtos);
    }

    /**
     * 리뷰 수정
     */
    @PatchMapping("/{movieId}/reviews/{reviewId}")
    public ResponseEntity<DataResponseDto<ReviewResponseDto>> updateReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long movieId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequestDto requestDto
    ) {
        ReviewResponseDto responseDto = reviewService.updateReview(userDetails.getUser(),
                requestDto, reviewId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/{movieId}/reviews/{reviewId}")
    public ResponseEntity<MessageResponseDto> deleteReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long movieId,
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(userDetails.getUser(), reviewId);
        return ResponseUtils.success();
    }
}