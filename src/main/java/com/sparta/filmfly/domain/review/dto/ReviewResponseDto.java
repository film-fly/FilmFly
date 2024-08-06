package com.sparta.filmfly.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.filmfly.domain.review.entity.Review;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewResponseDto {

    private Long id;
    private Long userId;
    private Long movieId;
    private String nickname;
    private String pictureUrl;
    private Float rating;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private Long goodCount;
    private Long badCount;

    public static ReviewResponseDto fromEntity(Review review, Long goodCount, Long badCount) {
        return ReviewResponseDto.builder()
            .id(review.getId())
            .userId(review.getUser().getId())
            .movieId(review.getMovie().getId())
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

    public static ReviewResponseDto fromEntity(Review review) {
        return fromEntity(review, 0L, 0L);
    }
}