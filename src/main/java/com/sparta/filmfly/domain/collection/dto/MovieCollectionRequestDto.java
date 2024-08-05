package com.sparta.filmfly.domain.collection.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MovieCollectionRequestDto {

    @NotNull(message = "보관함을 선택해주세요.")
    private Long collectionId;

    @NotNull(message = "영화를 선택해주세요.")
    private Long movieId;
}