package com.sparta.filmfly.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.filmfly.domain.board.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardResponseDto {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    private Long goodCount;
    private Long badCount;
    private Long hits;

    public static BoardResponseDto fromEntity(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .userId(board.getUser().getId())
                .title(board.getTitle())
                .content(board.getContent())
                .nickname(board.getUser().getNickname())
                .createdAt(board.getCreatedAt())
                .goodCount(0L)
                .badCount(0L)
                .hits(board.getHits())
                .build();
    }

    /**
     * 보드가 미디어 정보를 가지고 있으면 추가
     */
    /*public void addMediaDto(MediaResponseDto media) {
        if (mediaList == null) {
            mediaList = new ArrayList<>();
        }
        mediaList.add(media);
    }*/

    public void updateReactionCount(Long goodCount, Long badCount) {
        this.goodCount = goodCount;
        this.badCount = badCount;
    }

}