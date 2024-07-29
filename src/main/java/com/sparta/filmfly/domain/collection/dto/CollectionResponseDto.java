package com.sparta.filmfly.domain.collection.dto;

import com.sparta.filmfly.domain.collection.entity.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CollectionResponseDto {
    private Long collectionId;
    private String collectionName;
    private String collectionContent;

    public static CollectionResponseDto fromEntity(Collection collection) {
        return CollectionResponseDto.builder()
                .collectionId(collection.getId())
                .collectionName(collection.getName())
                .collectionContent(collection.getContent())
                .build();
    }
}
