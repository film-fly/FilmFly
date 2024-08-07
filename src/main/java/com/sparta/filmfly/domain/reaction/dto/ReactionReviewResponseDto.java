package com.sparta.filmfly.domain.reaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReactionReviewResponseDto {
    private Long id;
    private Long reviewId;
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
}