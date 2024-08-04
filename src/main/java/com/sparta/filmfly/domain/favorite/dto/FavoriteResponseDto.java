package com.sparta.filmfly.domain.favorite.dto;

import com.sparta.filmfly.domain.favorite.entity.Favorite;
import lombok.Builder;

@Builder
public class FavoriteResponseDto {

    private Long id;

    public static FavoriteResponseDto fromEntity(Favorite favorite) {
        return FavoriteResponseDto.builder()
            .id(favorite.getId())
            .build();
    }
}