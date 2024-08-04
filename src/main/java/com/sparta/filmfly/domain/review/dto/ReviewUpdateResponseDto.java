package com.sparta.filmfly.domain.review.dto;

import com.sparta.filmfly.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewUpdateResponseDto {

    private final Long id;
    private final String title;
    private final String content;

    public static ReviewUpdateResponseDto fromEntity(Review review) {
        return ReviewUpdateResponseDto.builder()
            .id(review.getId())
            .title(review.getTitle())
            .content(review.getContent())
            .build();
    }
}