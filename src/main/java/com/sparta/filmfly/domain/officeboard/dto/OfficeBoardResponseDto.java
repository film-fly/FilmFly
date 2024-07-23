package com.sparta.filmfly.domain.officeboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sparta.filmfly.domain.media.dto.MediaResponseDto;
import com.sparta.filmfly.domain.officeboard.entity.OfficeBoard;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OfficeBoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String nickName;
    private Long hits;
    private Long goodCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MediaResponseDto> mediaList;

    public static OfficeBoardResponseDto fromEntity(OfficeBoard officeBoard){
        return OfficeBoardResponseDto.builder()
                .id(officeBoard.getId())
                .title(officeBoard.getTitle())
                .content(officeBoard.getContent())
                .nickName(officeBoard.getUser().getNickname())
                .hits(officeBoard.getHits())
                .goodCount(officeBoard.getGoodCount())
                .createdAt(officeBoard.getUpdatedAt())
                .build();
    }

    public void addMediaDto(MediaResponseDto media){
        if(mediaList == null){
            mediaList = new ArrayList<>();
        }
        mediaList.add(media);
    }
}