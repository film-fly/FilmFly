package com.sparta.filmfly.domain.review.controller;

import com.sparta.filmfly.domain.review.dto.ReviewCreateRequestDto;
import com.sparta.filmfly.domain.review.dto.ReviewResponseDto;
import com.sparta.filmfly.domain.review.dto.ReviewUpdateRequestDto;
import com.sparta.filmfly.domain.review.service.ReviewService;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import com.sparta.filmfly.global.util.PageUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<DataResponseDto<ReviewResponseDto>> saveReview(
        @RequestBody ReviewCreateRequestDto requestDto
    ) {
        ReviewResponseDto responseDto = reviewService.saveReview(requestDto);
        return ResponseUtils.success(responseDto);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<DataResponseDto<ReviewResponseDto>> findReview(
        @PathVariable Long reviewId
    ) {
        ReviewResponseDto responseDto = reviewService.findReview(reviewId);
        return ResponseUtils.success(responseDto);
    }

    @GetMapping
    public ResponseEntity<DataResponseDto<List<ReviewResponseDto>>> findReviews(
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(required = false, defaultValue = "5") int size,
        @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
        @RequestParam(required = false, defaultValue = "false") boolean isAsc
    ) {
        Pageable pageable = PageUtils.of(page, size, sortBy, isAsc);
        List<ReviewResponseDto> responseDtos = reviewService.findReviews(1L, pageable); // 영화 id
        return ResponseUtils.success(responseDtos);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<DataResponseDto<ReviewResponseDto>> updateReview(
        @RequestBody ReviewUpdateRequestDto requestDto,
        @PathVariable Long reviewId
    ) {
        ReviewResponseDto responseDto = reviewService.updateReview(requestDto, reviewId);
        return ResponseUtils.success(responseDto);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<MessageResponseDto> deleteReview(
        @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(reviewId);
        return ResponseUtils.success();
    }
}