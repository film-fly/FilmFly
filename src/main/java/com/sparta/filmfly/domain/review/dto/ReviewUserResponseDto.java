package com.sparta.filmfly.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.filmfly.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReviewUserResponseDto {

    private Long id;
    private Long userId;
    private Long movieId;
    private String movieTitle;
    private String nickname;
    private String pictureUrl;
    private Float rating;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    private Long goodCount;
    private Long badCount;

    public static ReviewUserResponseDto fromEntity(Review review, Long goodCount, Long badCount) {
        return ReviewUserResponseDto.builder()
            .id(review.getId())
            .userId(review.getUser().getId())
            .movieId(review.getMovie().getId())
            .movieTitle(review.getMovie().getTitle())
            .nickname(review.getUser().getNickname())
            .pictureUrl(review.getUser().getPictureUrl())
            .rating(review.getRating())
            .title(review.getTitle())
            .content(review.getContent())
            .goodCount(goodCount)
            .badCount(badCount)
            .createdAt(review.getCreatedAt())
            .build();
    }

    public static ReviewUserResponseDto fromEntity(Review review) {
        return fromEntity(review, 0L, 0L);
    }
}