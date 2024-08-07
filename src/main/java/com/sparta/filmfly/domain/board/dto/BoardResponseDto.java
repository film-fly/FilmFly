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
    private String profileUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    private Long goodCount;
    private Long badCount;
    private Long hits;

    public static BoardResponseDto fromEntity(Board board,Long goodCount,Long badCount) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .userId(board.getUser().getId())
                .title(board.getTitle())
                .content(board.getContent())
                .nickname(board.getUser().getNickname())
            .profileUrl(board.getUser().getPictureUrl())
                .createdAt(board.getCreatedAt())
                .goodCount(goodCount)
                .badCount(badCount)
                .hits(board.getHits())
                .build();
    }
}