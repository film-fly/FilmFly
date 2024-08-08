package com.sparta.filmfly.domain.collection.dto;

import com.sparta.filmfly.domain.collection.entity.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CollectionWithUserResponseDto {
    private Long collectionId;
    private Long userId; // 생성 완료 후에 userId 필요해서 따로 만들었습니다.  일반적인 조회는 필요 없어서 불필요한 select
    private String collectionName;
    private String collectionContent;

    public static CollectionWithUserResponseDto fromEntity(Collection collection) {
        return CollectionWithUserResponseDto.builder()
                .collectionId(collection.getId())
                .userId(collection.getUser().getId())
                .collectionName(collection.getName())
                .collectionContent(collection.getContent())
                .build();
    }
}
