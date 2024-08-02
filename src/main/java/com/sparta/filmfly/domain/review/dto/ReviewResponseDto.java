package com.sparta.filmfly.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.filmfly.domain.review.entity.Review;
import com.sparta.filmfly.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponseDto {

    private Long id;
    private String nickname;
    private String pictureUrl;
    private Float rating;
    private String title;
    private String content;
    private Long goodCount;
    private Long badCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static ReviewResponseDto fromEntity(User user, Review review) {
        return ReviewResponseDto.builder()
            .id(review.getId())
            .nickname(user.getNickname())
            .pictureUrl(user.getPictureUrl())
            .rating(review.getRating())
            .title(review.getTitle())
            .content(review.getContent())
            .goodCount(review.getGoodCount())
            .badCount(review.getBadCount())
            .createdAt(review.getUpdatedAt())
            .build();
    }
}