package com.sparta.filmfly.domain.officeboard.dto;

import com.sparta.filmfly.domain.officeboard.entity.OfficeBoard;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OfficeBoardUpdateResponseDto {
    private Long id;
    private String title;
    private String content;

    public static OfficeBoardUpdateResponseDto fromEntity(OfficeBoard officeBoard) {
        return OfficeBoardUpdateResponseDto.builder()
                .id(officeBoard.getId())
                .title(officeBoard.getTitle())
                .content(officeBoard.getContent())
                .build();
    }
}
