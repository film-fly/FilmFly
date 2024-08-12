package com.sparta.filmfly.domain.collection.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.filmfly.domain.collection.entity.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CollectionStatusResponseDto {
    private Long collectionId;
    private String collectionName;
    private String collectionContent;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isRegistered; // 보관함에 등록된 영화인지 아닌지 true false

    public static CollectionStatusResponseDto fromEntity(Collection collection,Boolean isRegistered) {
        return CollectionStatusResponseDto.builder()
                .collectionId(collection.getId())
                .collectionName(collection.getName())
                .collectionContent(collection.getContent())
                .isRegistered(isRegistered)
                .build();
    }
}
