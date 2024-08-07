package com.sparta.filmfly.domain.movie.dto;

import com.sparta.filmfly.domain.movie.entity.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GenresResponseDto {
    private Long id;
    private String name;

    public Genre toEntity() {
        return Genre.builder()
                .genreId(id)
                .name(name)
                .build();
    }
    public static GenresResponseDto fromEntity(Genre genre) {
        return GenresResponseDto.builder()
                .id(genre.getGenreId())
                .name(genre.getName())
                .build();
    }
}
