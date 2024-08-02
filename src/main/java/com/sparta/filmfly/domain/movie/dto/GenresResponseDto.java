package com.sparta.filmfly.domain.movie.dto;

import com.sparta.filmfly.domain.movie.entity.Genre;
import lombok.Getter;

@Getter
public class GenresResponseDto {
    private Long id;
    private String name;

    public Genre toEntity() {
        return Genre.builder()
                .genreId(id)
                .name(name)
                .build();
    }
}
