package com.sparta.filmfly.domain.reaction.dto;

import com.sparta.filmfly.domain.movie.dto.MovieReactionCheckResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class ReactionCheckResponseDto {

    private Boolean isGood;
    private Boolean isBad;

    public static ReactionCheckResponseDto setupFalse() {
        return ReactionCheckResponseDto.builder()
            .isGood(false)
            .isBad(false)
            .build();
    }
}