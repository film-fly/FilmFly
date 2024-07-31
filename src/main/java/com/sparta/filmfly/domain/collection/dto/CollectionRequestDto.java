package com.sparta.filmfly.domain.collection.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CollectionRequestDto {
    @NotBlank
    private String name;
    private String content;
}