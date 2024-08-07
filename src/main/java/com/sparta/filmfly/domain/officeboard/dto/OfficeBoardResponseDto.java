package com.sparta.filmfly.domain.officeboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.filmfly.domain.officeboard.entity.OfficeBoard;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OfficeBoardResponseDto {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String nickName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private Long hits;

    public static OfficeBoardResponseDto fromEntity(OfficeBoard officeBoard){
        return OfficeBoardResponseDto.builder()
                .id(officeBoard.getId())
                .userId(officeBoard.getUser().getId())
                .title(officeBoard.getTitle())
                .content(officeBoard.getContent())
                .nickName(officeBoard.getUser().getNickname())
                .createdAt(officeBoard.getUpdatedAt())
                .hits(officeBoard.getHits())
                .build();
    }
}