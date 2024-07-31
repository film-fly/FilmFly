package com.sparta.filmfly.domain.reaction.dto;

import com.sparta.filmfly.domain.reaction.entity.Bad;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BadResponseDto {

    private Long id;

    public static BadResponseDto fromEntity(Bad bad) {
        return BadResponseDto.builder()
                .id(bad.getId())
                .build();
    }

}
