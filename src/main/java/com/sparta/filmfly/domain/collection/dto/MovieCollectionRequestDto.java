package com.sparta.filmfly.domain.collection.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MovieCollectionRequestDto {

    @NotBlank(message = "보관함을 선택해주세요.")
    private Long collectionId;

    @NotBlank(message = "영화를 선택해주세요.")
    private Long movieId;
}