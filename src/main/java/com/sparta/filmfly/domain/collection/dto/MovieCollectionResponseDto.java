package com.sparta.filmfly.domain.collection.dto;

import com.sparta.filmfly.domain.collection.entity.Collection;
import com.sparta.filmfly.domain.movie.dto.MovieResponseDto;
import com.sparta.filmfly.domain.movie.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MovieCollectionResponseDto {
    private Long collectionId;
    private String collectionName;
    private String collectionContent;
    private List<MovieResponseDto> movieResponseDtoList;

    public static MovieCollectionResponseDto fromEntity(Collection collection, List<Movie> movieList) {
        return MovieCollectionResponseDto.builder()
                .collectionId(collection.getId())
                .collectionName(collection.getName())
                .collectionContent(collection.getContent())
                .movieResponseDtoList(movieList.stream().map(
                        MovieResponseDto::fromEntity
                ).toList())
                .build();
    }
}
