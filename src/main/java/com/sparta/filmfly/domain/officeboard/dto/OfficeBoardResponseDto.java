package com.sparta.filmfly.domain.officeboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.filmfly.domain.officeboard.entity.OfficeBoard;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OfficeBoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long hits;
    private Long goodCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static OfficeBoardResponseDto fromEntity(OfficeBoard officeBoard){
        return OfficeBoardResponseDto.builder()
                .id(officeBoard.getId())
                .title(officeBoard.getTitle())
                .content(officeBoard.getContent())
                .hits(officeBoard.getHits())
                .goodCount(officeBoard.getGoodCount())
                .createdAt(officeBoard.getUpdatedAt())
                .build();
    }
}