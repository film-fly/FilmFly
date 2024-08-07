package com.sparta.filmfly.domain.review.dto;

import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.review.entity.Review;
import com.sparta.filmfly.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReviewCreateRequestDto {

    @NotBlank(message = "제목을 입력해 주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;
    @NotNull(message = "별점을 입력해 주세요.")
    private Float rating;

    public Review toEntity(User user, Movie movie) {
        return Review.builder()
            .title(this.title)
            .content(this.content)
            .rating(this.rating)
            .movie(movie)
            .user(user)
            .build();
    }
}