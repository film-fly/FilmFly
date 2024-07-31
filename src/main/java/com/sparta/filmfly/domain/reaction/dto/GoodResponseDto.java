package com.sparta.filmfly.domain.reaction.dto;

import com.sparta.filmfly.domain.reaction.entity.Good;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoodResponseDto {

    private Long id;

    public static GoodResponseDto fromEntity(Good good) {
        return GoodResponseDto.builder()
                .id(good.getId())
                .build();
    }

}
