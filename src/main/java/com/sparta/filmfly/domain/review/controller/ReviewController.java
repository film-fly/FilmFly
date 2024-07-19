package com.sparta.filmfly.domain.review.controller;

import com.sparta.filmfly.domain.review.dto.ReviewCreateRequestDto;
import com.sparta.filmfly.domain.review.dto.ReviewResponseDto;
import com.sparta.filmfly.domain.review.service.ReviewService;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        log.info("requestDto: {}", requestDto);
        ReviewResponseDto responseDto = reviewService.saveReview(requestDto);
        return ResponseUtils.success(responseDto);
    }
}