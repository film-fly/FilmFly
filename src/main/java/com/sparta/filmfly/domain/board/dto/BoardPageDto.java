package com.sparta.filmfly.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.filmfly.domain.board.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardPageDto {
    private Long id;
    private Long userId;
    private String title;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    private Long goodCount;
    private Long badCount;
    private Long hits;
    private Boolean isOwner;

    @Builder
    public BoardPageDto(Long id, Long userId, String title, String nickname, LocalDateTime createdAt, Long goodCount, Long badCount, Long hits) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.goodCount = goodCount;
        this.badCount = badCount;
        this.hits = hits;
        this.isOwner = false;
    }

    public static BoardPageDto fromEntity(Board board, Long goodCount) {
        return BoardPageDto.builder()
                .id(board.getId())
                .userId(board.getUser().getId())
                .title(board.getTitle())
                .nickname(board.getUser().getNickname())
                .createdAt(board.getCreatedAt())
                .goodCount(goodCount)
                .badCount(goodCount)
                .hits(goodCount)
                .build();
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }
}