package com.sparta.filmfly.domain.media.dto;

import com.sparta.filmfly.domain.media.entity.Media;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MediaResponseDto {
    private String url;
    private String fileName;

    public static MediaResponseDto fromEntity(Media media) {
        return MediaResponseDto.builder()
                .url(media.getS3Url())
                .fileName(media.getFileName())
                .build();
    }
}
