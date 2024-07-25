package com.sparta.filmfly.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.media.dto.MediaResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    private Long goodCount;
    private Long badCount;
    private Long hits;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MediaResponseDto> mediaList;

    public static BoardResponseDto fromEntity(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .nickname(board.getUser().getNickname())
                .createdAt(board.getCreatedAt())
                .goodCount(board.getGoodCount())
                .badCount(board.getBadCount())
                .hits(board.getHits())
                .build();
    }

    /**
     * 보드가 미디어 정보를 가지고 있으면 추가
     */
    public void addMediaDto(MediaResponseDto media) {
        if (mediaList == null) {
            mediaList = new ArrayList<>();
        }
        mediaList.add(media);
    }

}